package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.dto.product.ProductDetailV2RespDTO;
import com.lin.csln.dto.product.ProductPageV2RespDTO;
import com.lin.csln.dto.product.ProductV2QueryParamDTO;
import com.lin.csln.dto.product.ProductSkuV2RespDTO;
import com.lin.csln.dto.product.ProductV2DTO;
import com.lin.csln.entity.ProductDimImageConfigDO;
import com.lin.csln.entity.ProductDimImageDO;
import com.lin.csln.entity.ProductSkuDimDO;
import com.lin.csln.entity.ProductSkuDO;
import com.lin.csln.entity.ProductV2DO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.mapper.ProductDimImageConfigMapper;
import com.lin.csln.mapper.ProductDimImageMapper;
import com.lin.csln.mapper.ProductSkuDimMapper;
import com.lin.csln.mapper.ProductV2Mapper;
import com.lin.csln.service.ProductSkuService;
import com.lin.csln.service.ProductV2AttrService;
import com.lin.csln.service.ProductV2Service;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductV2ServiceImpl extends BaseReadonlyServiceImpl<ProductV2Mapper, ProductV2DO> implements ProductV2Service {

    @Resource
    private ProductSkuService productSkuService;
    @Resource
    private ProductV2AttrService productV2AttrService;
    @Resource
    private ProductSkuDimMapper productSkuDimMapper;
    @Resource
    private ProductDimImageConfigMapper productDimImageConfigMapper;
    @Resource
    private ProductDimImageMapper productDimImageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addProductV2(ProductV2DTO productDTO) {
        ProductV2DO product = new ProductV2DO();
        BeanUtils.copyProperties(productDTO, product);
        baseMapper.insert(product);

        String productId = product.getId();
        productSkuService.saveProductSkuV2(productId, productDTO.getSkuList(), productDTO.getMountDimKeys());
        productV2AttrService.saveProductV2Attrs(productId, productDTO.getAttrList());
        return productId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProductV2(String productId, ProductV2DTO productDTO) {
        ProductV2DO product = baseMapper.selectById(productId);
        if (product == null || Objects.equals(product.getIsDelete(), GlobalEnums.YES.getCode())) {
            throw new BusinessException("更新失败，产品数据不存在。");
        }
        BeanUtils.copyProperties(productDTO, product, "id", "isDelete");
        baseMapper.updateById(product);

        productSkuService.saveProductSkuV2(productId, productDTO.getSkuList(), productDTO.getMountDimKeys());
        productV2AttrService.saveProductV2Attrs(productId, productDTO.getAttrList());
        return true;
    }

    @Override
    public ProductDetailV2RespDTO getProductByIdV2(String productId) {
        ProductV2DO product = baseMapper.selectById(productId);
        if (product == null || Objects.equals(product.getIsDelete(), GlobalEnums.YES.getCode())) {
            throw new BusinessException("商品不存在或已删除。");
        }

        List<ProductSkuV2RespDTO> skuList = productSkuService.listSkuWithDimsByProductId(productId);
        ProductDetailV2RespDTO respDTO = new ProductDetailV2RespDTO();
        BeanUtils.copyProperties(product, respDTO);
        respDTO.setSkuList(skuList);
        respDTO.setMountDimKeys(productSkuService.listMountDimKeysByProductId(productId));
        respDTO.setAttrList(productV2AttrService.listProductV2Attrs(productId));
        return respDTO;
    }

    @Override
    public PageRespDTO<ProductPageV2RespDTO> pageProductV2(ProductV2QueryParamDTO queryDTO) {
        ProductV2QueryParamDTO query = queryDTO == null ? new ProductV2QueryParamDTO() : queryDTO;
        IPage<ProductV2DO> page = new Page<>(query.getPage(), query.getLimit());
        LambdaQueryWrapper<ProductV2DO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductV2DO::getIsDelete, GlobalEnums.NO.getCode());
        wrapper.like(StringUtils.hasText(query.getProductNo()), ProductV2DO::getProductNo, query.getProductNo());
        wrapper.like(StringUtils.hasText(query.getName()), ProductV2DO::getName, query.getName());
        wrapper.eq(query.getStatus() != null, ProductV2DO::getStatus, query.getStatus());
        wrapper.eq(StringUtils.hasText(query.getCategoryId()), ProductV2DO::getCategoryId, query.getCategoryId());
        wrapper.orderByDesc(ProductV2DO::getCreateTime);

        IPage<ProductV2DO> result = baseMapper.selectPage(page, wrapper);
        IPage<ProductPageV2RespDTO> respPage = result.convert(item -> {
            ProductPageV2RespDTO dto = new ProductPageV2RespDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        });
        return PageRespDTO.build(respPage, query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProductV2(String productId) {
        ProductV2DO product = baseMapper.selectById(productId);
        if (product == null || Objects.equals(product.getIsDelete(), GlobalEnums.YES.getCode())) {
            throw new BusinessException("删除失败，商品不存在。");
        }

        LambdaQueryWrapper<ProductSkuDO> skuWrapper = new LambdaQueryWrapper<>();
        skuWrapper.eq(ProductSkuDO::getProductV2Id, productId)
                .eq(ProductSkuDO::getIsDelete, GlobalEnums.NO.getCode());
        List<ProductSkuDO> skuList = productSkuService.list(skuWrapper);
        List<String> skuIds = skuList.stream().map(ProductSkuDO::getId).collect(Collectors.toList());
        if (productSkuService.hasOccupiedOrReferencedSkuIds(skuIds)) {
            throw new BusinessException("存在已占用SKU，禁止删除商品");
        }

        if (!skuList.isEmpty()) {
            for (ProductSkuDO sku : skuList) {
                sku.setIsDelete(GlobalEnums.YES.getCode());
            }
            productSkuService.updateBatchById(skuList);
        }
        softDeleteSkuDims(skuIds);
        softDeleteDimConfigAndImages(productId);
        productV2AttrService.softDeleteByProductV2Id(productId);

        product.setIsDelete(GlobalEnums.YES.getCode());
        baseMapper.updateById(product);
        return true;
    }

    private void softDeleteSkuDims(List<String> skuIds) {
        if (skuIds == null || skuIds.isEmpty()) {
            return;
        }
        LambdaQueryWrapper<ProductSkuDimDO> dimWrapper = new LambdaQueryWrapper<>();
        dimWrapper.in(ProductSkuDimDO::getSkuId, skuIds)
                .eq(ProductSkuDimDO::getIsDelete, GlobalEnums.NO.getCode());
        List<ProductSkuDimDO> dimList = productSkuDimMapper.selectList(dimWrapper);
        if (dimList.isEmpty()) {
            return;
        }
        for (ProductSkuDimDO dim : dimList) {
            dim.setIsDelete(GlobalEnums.YES.getCode());
            productSkuDimMapper.updateById(dim);
        }
    }

    private void softDeleteDimConfigAndImages(String productId) {
        LambdaQueryWrapper<ProductDimImageConfigDO> configWrapper = new LambdaQueryWrapper<>();
        configWrapper.eq(ProductDimImageConfigDO::getProductId, productId)
                .eq(ProductDimImageConfigDO::getIsDelete, GlobalEnums.NO.getCode());
        List<ProductDimImageConfigDO> configList = productDimImageConfigMapper.selectList(configWrapper);
        for (ProductDimImageConfigDO config : configList) {
            config.setIsDelete(GlobalEnums.YES.getCode());
            productDimImageConfigMapper.updateById(config);
        }

        LambdaQueryWrapper<ProductDimImageDO> imageWrapper = new LambdaQueryWrapper<>();
        imageWrapper.eq(ProductDimImageDO::getProductId, productId)
                .eq(ProductDimImageDO::getIsDelete, GlobalEnums.NO.getCode());
        List<ProductDimImageDO> imageList = productDimImageMapper.selectList(imageWrapper);
        for (ProductDimImageDO image : imageList) {
            image.setIsDelete(GlobalEnums.YES.getCode());
            productDimImageMapper.updateById(image);
        }
    }
}
