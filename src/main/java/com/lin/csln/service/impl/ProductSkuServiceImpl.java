package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.dto.product.ProductColorDTO;
import com.lin.csln.dto.product.ProductSkuDTO;
import com.lin.csln.dto.product.ProductSkuListDTO;
import com.lin.csln.entity.ProductSkuDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.mapper.ProductSkuMapper;
import com.lin.csln.service.ProductSkuService;
import com.lin.csln.service.StockService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商品SKU服务实现
 */
@Service
public class ProductSkuServiceImpl extends BaseReadonlyServiceImpl<ProductSkuMapper, ProductSkuDO> implements ProductSkuService {

    @Resource
    private StockService stockService;

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
}
