package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.dto.product.ProductColorDTO;
import com.lin.csln.dto.product.ProductSkuDTO;
import com.lin.csln.dto.product.ProductSkuListDTO;
import com.lin.csln.dto.product.ProductSkuDimV2DTO;
import com.lin.csln.dto.product.ProductSkuV2RespDTO;
import com.lin.csln.dto.product.ProductSkuV2DTO;
import com.lin.csln.entity.ProductDimImageConfigDO;
import com.lin.csln.entity.ProductDimImageDO;
import com.lin.csln.entity.ProductSkuDimDO;
import com.lin.csln.entity.ProductSkuDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.mapper.ProductDimImageConfigMapper;
import com.lin.csln.mapper.ProductDimImageMapper;
import com.lin.csln.mapper.ProductSkuDimMapper;
import com.lin.csln.mapper.ProductSkuMapper;
import com.lin.csln.service.ProductSkuService;
import com.lin.csln.service.StockService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商品SKU服务实现
 */
@Service
public class ProductSkuServiceImpl extends BaseReadonlyServiceImpl<ProductSkuMapper, ProductSkuDO> implements ProductSkuService {

    @Resource
    private StockService stockService;
    @Resource
    private ProductSkuDimMapper productSkuDimMapper;
    @Resource
    private ProductDimImageMapper productDimImageMapper;
    @Resource
    private ProductDimImageConfigMapper productDimImageConfigMapper;

    @Override
    public List<ProductSkuDTO> listSkuWithStockByProductId(String productId) {
        return baseMapper.listSkuWithStockByProductId(productId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProductSku(String productId, List<ProductColorDTO> colorList, List<String> sizeNameList) {
        validateBaseInput(productId, colorList, sizeNameList);
        validateColorList(colorList);
        validateSizeNameList(sizeNameList);

        LambdaQueryWrapper<ProductSkuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductSkuDO::getProductId, productId);
        List<ProductSkuDO> existSkuList = baseMapper.selectList(queryWrapper);

        List<ProductSkuDO> needUpdateList = new ArrayList<>();
        List<ProductSkuDO> needInsertList = new ArrayList<>();
        Set<String> keepSkuIds = new HashSet<>();
        Set<String> usedSkuIds = new HashSet<>();

        // 仅按(color,size)匹配
        for (ProductColorDTO colorDTO : colorList) {
            if (colorDTO == null || !StringUtils.hasText(colorDTO.getColorName())) {
                throw new BusinessException("颜色名称不能为空");
            }
            String normalizedColorName = colorDTO.getColorName().trim();

            for (String sizeName : sizeNameList) {
                String normalizedSizeName = sizeName.trim();

                ProductSkuDO targetSku = findByColorAndSize(existSkuList, normalizedColorName, normalizedSizeName, usedSkuIds);
                if (targetSku == null) {
                    ProductSkuDO newSku = new ProductSkuDO();
                    newSku.setProductId(productId);
                    newSku.setColorName(normalizedColorName);
                    newSku.setSizeName(normalizedSizeName);
                    newSku.setIsDelete(GlobalEnums.NO.getCode());
                    needInsertList.add(newSku);
                    continue;
                }

                targetSku.setColorName(normalizedColorName);
                targetSku.setSizeName(normalizedSizeName);
                targetSku.setIsDelete(GlobalEnums.NO.getCode());
                needUpdateList.add(targetSku);
                keepSkuIds.add(targetSku.getId());
                usedSkuIds.add(targetSku.getId());
            }
        }

        if (!needInsertList.isEmpty()) {
            this.saveBatch(needInsertList);
        }
        if (!needUpdateList.isEmpty()) {
            this.updateBatchById(needUpdateList);
        }

        List<ProductSkuDO> deleteList = existSkuList.stream()
                .filter(sku -> !keepSkuIds.contains(sku.getId()))
                .filter(sku -> !GlobalEnums.YES.getCode().equals(sku.getIsDelete()))
                .collect(Collectors.toList());

        if (!deleteList.isEmpty()) {
            List<String> deleteSkuIds = deleteList.stream().map(ProductSkuDO::getId).collect(Collectors.toList());
            if (stockService.hasOccupiedStockBySkuIds(deleteSkuIds)) {
                throw new BusinessException("存在qty>0或lock_qty>0的SKU，禁止软删除");
            }
            for (ProductSkuDO sku : deleteList) {
                sku.setIsDelete(GlobalEnums.YES.getCode());
            }
            this.updateBatchById(deleteList);
        }
    }

    private void validateBaseInput(String productId, List<ProductColorDTO> colorList, List<String> sizeNameList) {
        if (!StringUtils.hasText(productId)) {
            throw new BusinessException("商品ID不能为空");
        }
        if (colorList == null || colorList.isEmpty()) {
            throw new BusinessException("颜色列表不能为空");
        }
        if (sizeNameList == null || sizeNameList.isEmpty()) {
            throw new BusinessException("尺码列表不能为空");
        }
    }

    private void validateColorList(List<ProductColorDTO> colorList) {
        Set<String> colorNameSet = new HashSet<>();
        for (ProductColorDTO colorDTO : colorList) {
            if (colorDTO == null || !StringUtils.hasText(colorDTO.getColorName())) {
                throw new BusinessException("颜色名称不能为空");
            }
            String normalizedColorName = colorDTO.getColorName().trim();
            if (!colorNameSet.add(normalizedColorName)) {
                throw new BusinessException("颜色不能重复：" + normalizedColorName);
            }
        }
    }

    private void validateSizeNameList(List<String> sizeNameList) {
        Set<String> sizeSet = new HashSet<>();
        for (String sizeName : sizeNameList) {
            if (!StringUtils.hasText(sizeName)) {
                throw new BusinessException("尺码名称不能为空");
            }
            String normalizedSizeName = sizeName.trim();
            if (!sizeSet.add(normalizedSizeName)) {
                throw new BusinessException("尺码不能重复：" + normalizedSizeName);
            }
        }
    }

    private ProductSkuDO findByColorAndSize(List<ProductSkuDO> existSkuList, String colorName, String sizeName, Set<String> usedSkuIds) {
        for (ProductSkuDO sku : existSkuList) {
            if (usedSkuIds.contains(sku.getId())) {
                continue;
            }
            if (colorName.equals(sku.getColorName()) && sizeName.equals(sku.getSizeName())) {
                return sku;
            }
        }
        return null;
    }

    @Override
    public List<ProductSkuListDTO> getSkuByProductId(String productId) {
        LambdaQueryWrapper<ProductSkuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductSkuDO::getProductId, productId);
        wrapper.eq(ProductSkuDO::getIsDelete, GlobalEnums.NO.getCode());
        List<ProductSkuDO> skuList = baseMapper.selectList(wrapper);

        return skuList.stream().map(sku -> {
            ProductSkuListDTO dto = new ProductSkuListDTO();
            BeanUtils.copyProperties(sku, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProductSkuV2(String productId, List<ProductSkuV2DTO> skuList, List<String> mountDimKeys) {
        if (!StringUtils.hasText(productId)) {
            throw new BusinessException("商品ID不能为空");
        }
        if (skuList == null || skuList.isEmpty()) {
            throw new BusinessException("SKU列表不能为空");
        }

        List<NormalizedSku> normalizedSkuList = skuList.stream()
                .map(this::normalizeSku)
                .collect(Collectors.toList());

        Set<String> signatureSet = new HashSet<>();
        for (NormalizedSku sku : normalizedSkuList) {
            if (!signatureSet.add(sku.signature)) {
                throw new BusinessException("同商品下维度组合不能重复");
            }
        }

        LambdaQueryWrapper<ProductSkuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductSkuDO::getProductV2Id, productId);
        List<ProductSkuDO> existSkuList = baseMapper.selectList(queryWrapper);
        Map<String, ProductSkuDO> existById = existSkuList.stream()
                .collect(Collectors.toMap(ProductSkuDO::getId, i -> i, (a, b) -> a));

        Map<String, List<ProductSkuDO>> existBySignature = existSkuList.stream()
                .collect(Collectors.groupingBy(ProductSkuDO::getDimensionSignature));

        Set<String> usedSkuIds = new HashSet<>();
        Set<String> keepSkuIds = new HashSet<>();
        Map<String, List<ProductSkuDimV2DTO>> skuDimsToSave = new HashMap<>();
        List<ProductSkuDO> needInsertList = new ArrayList<>();
        List<ProductSkuDO> needUpdateList = new ArrayList<>();

        for (NormalizedSku normalizedSku : normalizedSkuList) {
            ProductSkuDO targetSku = null;
            if (StringUtils.hasText(normalizedSku.skuId)) {
                ProductSkuDO skuById = existById.get(normalizedSku.skuId);
                if (skuById != null && !usedSkuIds.contains(skuById.getId())) {
                    targetSku = skuById;
                }
            }
            if (targetSku == null) {
                targetSku = pickSkuBySignature(existBySignature.get(normalizedSku.signature), usedSkuIds);
            }
            if (targetSku == null) {
                ProductSkuDO newSku = new ProductSkuDO();
                newSku.setProductV2Id(productId);
                newSku.setBarcode(resolveNewBarcode(normalizedSku.barcode, null));
                newSku.setDimensionSignature(normalizedSku.signature);
                newSku.setIsDelete(GlobalEnums.NO.getCode());
                needInsertList.add(newSku);
            } else {
                targetSku.setProductId(StringUtils.hasText(targetSku.getProductId()) ? targetSku.getProductId() : "");
                targetSku.setProductV2Id(productId);
                if (StringUtils.hasText(normalizedSku.barcode)) {
                    targetSku.setBarcode(resolveNewBarcode(normalizedSku.barcode, targetSku.getId()));
                }
                targetSku.setDimensionSignature(normalizedSku.signature);
                targetSku.setIsDelete(GlobalEnums.NO.getCode());
                needUpdateList.add(targetSku);
                usedSkuIds.add(targetSku.getId());
                keepSkuIds.add(targetSku.getId());
                skuDimsToSave.put(targetSku.getId(), normalizedSku.dims);
            }
        }

        if (!needInsertList.isEmpty()) {
            this.saveBatch(needInsertList);
            for (int i = 0; i < needInsertList.size(); i++) {
                ProductSkuDO savedSku = needInsertList.get(i);
                NormalizedSku normalizedSku = normalizedSkuList.stream()
                        .filter(item -> item.signature.equals(savedSku.getDimensionSignature()))
                        .findFirst()
                        .orElse(null);
                if (normalizedSku != null) {
                    skuDimsToSave.put(savedSku.getId(), normalizedSku.dims);
                }
                keepSkuIds.add(savedSku.getId());
            }
        }
        if (!needUpdateList.isEmpty()) {
            this.updateBatchById(needUpdateList);
        }

        List<ProductSkuDO> deleteList = existSkuList.stream()
                .filter(sku -> !keepSkuIds.contains(sku.getId()))
                .filter(sku -> !GlobalEnums.YES.getCode().equals(sku.getIsDelete()))
                .collect(Collectors.toList());

        if (!deleteList.isEmpty()) {
            List<String> deleteSkuIds = deleteList.stream().map(ProductSkuDO::getId).collect(Collectors.toList());
            boolean occupiedByStock = stockService.hasOccupiedStockBySkuIds(deleteSkuIds);
            Integer refCount = baseMapper.countDocumentReferenceBySkuIds(deleteSkuIds);
            if (occupiedByStock || (refCount != null && refCount > 0)) {
                throw new BusinessException("存在已占用SKU，禁止删除");
            }
            for (ProductSkuDO sku : deleteList) {
                sku.setIsDelete(GlobalEnums.YES.getCode());
            }
            this.updateBatchById(deleteList);
            softDeleteDims(deleteSkuIds);
        }

        replaceSkuDims(skuDimsToSave);
        replaceMountDimConfig(productId, mountDimKeys);
        replaceDimImages(productId, normalizedSkuList, mountDimKeys);
    }

    @Override
    public List<ProductSkuV2RespDTO> listSkuWithDimsByProductId(String productId) {
        LambdaQueryWrapper<ProductSkuDO> skuWrapper = new LambdaQueryWrapper<>();
        skuWrapper.eq(ProductSkuDO::getProductV2Id, productId)
                .eq(ProductSkuDO::getIsDelete, GlobalEnums.NO.getCode());
        List<ProductSkuDO> skuBaseList = baseMapper.selectList(skuWrapper);
        if (skuBaseList == null || skuBaseList.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> skuIds = skuBaseList.stream().map(ProductSkuDO::getId).collect(Collectors.toList());

        LambdaQueryWrapper<ProductSkuDimDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ProductSkuDimDO::getSkuId, skuIds)
                .eq(ProductSkuDimDO::getIsDelete, GlobalEnums.NO.getCode())
                .orderByAsc(ProductSkuDimDO::getDimOrder, ProductSkuDimDO::getId);
        List<ProductSkuDimDO> dimList = productSkuDimMapper.selectList(wrapper);

        Map<String, List<ProductSkuDimV2DTO>> dimMap = dimList.stream().collect(Collectors.groupingBy(
                ProductSkuDimDO::getSkuId,
                Collectors.mapping(this::toDimDTO, Collectors.toList())
        ));
        Map<String, String> imageMap = listDimImageMapByProductId(productId);
        Set<String> mountDimKeySet = new HashSet<>(listMountDimKeysByProductId(productId));
        for (List<ProductSkuDimV2DTO> dims : dimMap.values()) {
            for (ProductSkuDimV2DTO dim : dims) {
                if (!mountDimKeySet.contains(dim.getKey())) {
                    continue;
                }
                String imageKey = buildDimImageKey(dim.getKey(), dim.getValue());
                dim.setImageFileId(imageMap.get(imageKey));
            }
        }
        List<ProductSkuV2RespDTO> respList = new ArrayList<>();
        for (ProductSkuDO base : skuBaseList) {
            ProductSkuV2RespDTO dto = new ProductSkuV2RespDTO();
            dto.setId(base.getId());
            dto.setProductId(base.getProductId());
            dto.setBarcode(base.getBarcode());
            dto.setDims(dimMap.getOrDefault(base.getId(), new ArrayList<>()));
            respList.add(dto);
        }
        return respList;
    }

    @Override
    public List<String> listMountDimKeysByProductId(String productId) {
        LambdaQueryWrapper<ProductDimImageConfigDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductDimImageConfigDO::getProductId, productId)
                .eq(ProductDimImageConfigDO::getIsDelete, GlobalEnums.NO.getCode());
        List<ProductDimImageConfigDO> configList = productDimImageConfigMapper.selectList(wrapper);
        return configList.stream().map(ProductDimImageConfigDO::getDimKey).collect(Collectors.toList());
    }

    @Override
    public boolean hasOccupiedOrReferencedSkuIds(List<String> skuIds) {
        if (skuIds == null || skuIds.isEmpty()) {
            return false;
        }
        boolean occupiedByStock = stockService.hasOccupiedStockBySkuIds(skuIds);
        Integer refCount = baseMapper.countDocumentReferenceBySkuIds(skuIds);
        return occupiedByStock || (refCount != null && refCount > 0);
    }

    private ProductSkuDimV2DTO toDimDTO(ProductSkuDimDO dimDO) {
        ProductSkuDimV2DTO dto = new ProductSkuDimV2DTO();
        dto.setKey(dimDO.getDimKey());
        dto.setName(dimDO.getDimName());
        dto.setValue(dimDO.getDimValue());
        dto.setOrder(dimDO.getDimOrder());
        return dto;
    }

    private ProductSkuDO pickSkuBySignature(List<ProductSkuDO> skuList, Set<String> usedSkuIds) {
        if (skuList == null || skuList.isEmpty()) {
            return null;
        }
        for (ProductSkuDO sku : skuList) {
            if (!usedSkuIds.contains(sku.getId()) && GlobalEnums.NO.getCode().equals(sku.getIsDelete())) {
                return sku;
            }
        }
        for (ProductSkuDO sku : skuList) {
            if (!usedSkuIds.contains(sku.getId())) {
                return sku;
            }
        }
        return null;
    }

    private NormalizedSku normalizeSku(ProductSkuV2DTO sku) {
        if (sku == null) {
            throw new BusinessException("SKU数据不能为空");
        }
        if (sku.getDims() == null || sku.getDims().isEmpty()) {
            throw new BusinessException("SKU维度不能为空");
        }

        List<ProductSkuDimV2DTO> dims = sku.getDims().stream()
                .map(this::normalizeDim)
                .sorted(Comparator.comparing(ProductSkuDimV2DTO::getOrder).thenComparing(ProductSkuDimV2DTO::getKey))
                .collect(Collectors.toList());

        Set<String> dimKeySet = new HashSet<>();
        for (ProductSkuDimV2DTO dim : dims) {
            if (!dimKeySet.add(dim.getKey())) {
                throw new BusinessException("同一SKU内维度键不能重复：" + dim.getKey());
            }
        }

        String signature = dims.stream()
                .map(dim -> dim.getKey() + "=" + dim.getValue())
                .collect(Collectors.joining("|"));

        NormalizedSku normalizedSku = new NormalizedSku();
        normalizedSku.skuId = StringUtils.hasText(sku.getId()) ? sku.getId().trim() : null;
        normalizedSku.barcode = StringUtils.hasText(sku.getBarcode()) ? sku.getBarcode().trim() : null;
        normalizedSku.signature = signature;
        normalizedSku.dims = dims;
        return normalizedSku;
    }

    private ProductSkuDimV2DTO normalizeDim(ProductSkuDimV2DTO dim) {
        if (dim == null) {
            throw new BusinessException("维度项不能为空");
        }
        if (!StringUtils.hasText(dim.getKey()) || !StringUtils.hasText(dim.getName()) || !StringUtils.hasText(dim.getValue())) {
            throw new BusinessException("维度key/name/value不能为空");
        }
        if (dim.getOrder() == null) {
            throw new BusinessException("维度顺序不能为空");
        }
        ProductSkuDimV2DTO normalized = new ProductSkuDimV2DTO();
        normalized.setKey(dim.getKey().trim());
        normalized.setName(dim.getName().trim());
        normalized.setValue(dim.getValue().trim());
        normalized.setOrder(dim.getOrder());
        normalized.setImageFileId(StringUtils.hasText(dim.getImageFileId()) ? dim.getImageFileId().trim() : null);
        return normalized;
    }

    private void replaceSkuDims(Map<String, List<ProductSkuDimV2DTO>> skuDimsToSave) {
        if (skuDimsToSave == null || skuDimsToSave.isEmpty()) {
            return;
        }
        List<String> skuIds = new ArrayList<>(skuDimsToSave.keySet());
        softDeleteDims(skuIds);

        List<ProductSkuDimDO> insertDimList = new ArrayList<>();
        for (Map.Entry<String, List<ProductSkuDimV2DTO>> entry : skuDimsToSave.entrySet()) {
            String skuId = entry.getKey();
            for (ProductSkuDimV2DTO dim : entry.getValue()) {
                ProductSkuDimDO dimDO = new ProductSkuDimDO();
                dimDO.setSkuId(skuId);
                dimDO.setDimKey(dim.getKey());
                dimDO.setDimName(dim.getName());
                dimDO.setDimValue(dim.getValue());
                dimDO.setDimOrder(dim.getOrder());
                dimDO.setIsDelete(GlobalEnums.NO.getCode());
                insertDimList.add(dimDO);
            }
        }
        if (!insertDimList.isEmpty()) {
            for (ProductSkuDimDO dimDO : insertDimList) {
                productSkuDimMapper.insert(dimDO);
            }
        }
    }

    private void softDeleteDims(List<String> skuIds) {
        if (skuIds == null || skuIds.isEmpty()) {
            return;
        }
        LambdaQueryWrapper<ProductSkuDimDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ProductSkuDimDO::getSkuId, skuIds)
                .eq(ProductSkuDimDO::getIsDelete, GlobalEnums.NO.getCode());
        List<ProductSkuDimDO> dimList = productSkuDimMapper.selectList(wrapper);
        if (dimList.isEmpty()) {
            return;
        }
        for (ProductSkuDimDO dimDO : dimList) {
            dimDO.setIsDelete(GlobalEnums.YES.getCode());
            productSkuDimMapper.updateById(dimDO);
        }
    }

    private static class NormalizedSku {
        private String skuId;
        private String barcode;
        private String signature;
        private List<ProductSkuDimV2DTO> dims;
    }

    private String resolveNewBarcode(String inputBarcode, String excludeSkuId) {
        if (StringUtils.hasText(inputBarcode)) {
            String normalized = inputBarcode.trim();
            ensureBarcodeAvailable(normalized, excludeSkuId);
            return normalized;
        }
        return generateBarcode();
    }

    private void ensureBarcodeAvailable(String barcode, String excludeSkuId) {
        LambdaQueryWrapper<ProductSkuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductSkuDO::getBarcode, barcode)
                .eq(ProductSkuDO::getIsDelete, GlobalEnums.NO.getCode());
        if (StringUtils.hasText(excludeSkuId)) {
            wrapper.ne(ProductSkuDO::getId, excludeSkuId);
        }
        if (baseMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("条码已存在：" + barcode);
        }
    }

    private String generateBarcode() {
        for (int i = 0; i < 8; i++) {
            String candidate = "V2" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            LambdaQueryWrapper<ProductSkuDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProductSkuDO::getBarcode, candidate)
                    .eq(ProductSkuDO::getIsDelete, GlobalEnums.NO.getCode());
            if (baseMapper.selectCount(wrapper) == 0) {
                return candidate;
            }
        }
        throw new BusinessException("条码生成失败，请重试");
    }

    private void replaceMountDimConfig(String productId, List<String> mountDimKeys) {
        LambdaQueryWrapper<ProductDimImageConfigDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductDimImageConfigDO::getProductId, productId)
                .eq(ProductDimImageConfigDO::getIsDelete, GlobalEnums.NO.getCode());
        List<ProductDimImageConfigDO> oldList = productDimImageConfigMapper.selectList(wrapper);
        if (!oldList.isEmpty()) {
            for (ProductDimImageConfigDO item : oldList) {
                item.setIsDelete(GlobalEnums.YES.getCode());
                productDimImageConfigMapper.updateById(item);
            }
        }

        if (mountDimKeys == null || mountDimKeys.isEmpty()) {
            return;
        }
        Set<String> keySet = new HashSet<>();
        for (String dimKey : mountDimKeys) {
            if (!StringUtils.hasText(dimKey)) {
                continue;
            }
            String normalizedKey = dimKey.trim();
            if (!keySet.add(normalizedKey)) {
                continue;
            }
            ProductDimImageConfigDO config = new ProductDimImageConfigDO();
            config.setProductId(productId);
            config.setDimKey(normalizedKey);
            config.setIsDelete(GlobalEnums.NO.getCode());
            productDimImageConfigMapper.insert(config);
        }
    }

    private void replaceDimImages(String productId, List<NormalizedSku> normalizedSkuList, List<String> mountDimKeys) {
        Set<String> mountDimKeySet = new HashSet<>();
        if (mountDimKeys != null) {
            for (String key : mountDimKeys) {
                if (StringUtils.hasText(key)) {
                    mountDimKeySet.add(key.trim());
                }
            }
        }

        Map<String, String> dimImageMap = new HashMap<>();
        for (NormalizedSku sku : normalizedSkuList) {
            for (ProductSkuDimV2DTO dim : sku.dims) {
                if (!mountDimKeySet.contains(dim.getKey()) || !StringUtils.hasText(dim.getImageFileId())) {
                    continue;
                }
                String imageKey = buildDimImageKey(dim.getKey(), dim.getValue());
                String oldFileId = dimImageMap.get(imageKey);
                if (oldFileId != null && !Objects.equals(oldFileId, dim.getImageFileId())) {
                    throw new BusinessException("同一维度值图片冲突：" + dim.getKey() + "=" + dim.getValue());
                }
                dimImageMap.put(imageKey, dim.getImageFileId());
            }
        }

        LambdaQueryWrapper<ProductDimImageDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductDimImageDO::getProductId, productId)
                .eq(ProductDimImageDO::getIsDelete, GlobalEnums.NO.getCode());
        List<ProductDimImageDO> oldList = productDimImageMapper.selectList(wrapper);
        if (!oldList.isEmpty()) {
            for (ProductDimImageDO item : oldList) {
                item.setIsDelete(GlobalEnums.YES.getCode());
                productDimImageMapper.updateById(item);
            }
        }

        for (Map.Entry<String, String> entry : dimImageMap.entrySet()) {
            String[] arr = entry.getKey().split("\\|", 2);
            ProductDimImageDO imageDO = new ProductDimImageDO();
            imageDO.setProductId(productId);
            imageDO.setDimKey(arr[0]);
            imageDO.setDimValue(arr[1]);
            imageDO.setFileId(entry.getValue());
            imageDO.setIsDelete(GlobalEnums.NO.getCode());
            productDimImageMapper.insert(imageDO);
        }
    }

    private Map<String, String> listDimImageMapByProductId(String productId) {
        LambdaQueryWrapper<ProductDimImageDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductDimImageDO::getProductId, productId)
                .eq(ProductDimImageDO::getIsDelete, GlobalEnums.NO.getCode());
        List<ProductDimImageDO> imageList = productDimImageMapper.selectList(wrapper);
        Map<String, String> result = new HashMap<>();
        for (ProductDimImageDO imageDO : imageList) {
            result.put(buildDimImageKey(imageDO.getDimKey(), imageDO.getDimValue()), imageDO.getFileId());
        }
        return result;
    }

    private String buildDimImageKey(String dimKey, String dimValue) {
        return dimKey + "|" + dimValue;
    }
}
