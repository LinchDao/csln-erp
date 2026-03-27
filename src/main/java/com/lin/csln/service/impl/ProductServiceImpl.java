package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.dto.product.*;
import com.lin.csln.entity.ProductDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.mapper.ProductMapper;
import com.lin.csln.service.ProductColorImageService;
import com.lin.csln.service.ProductService;
import com.lin.csln.service.ProductSkuService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 商品表 服务实现类
 *
 * @author 系统生成器
 */
@Service
@Transactional(readOnly = true)
public class ProductServiceImpl extends ServiceImpl<ProductMapper, ProductDO> implements ProductService {

    @Resource
    private ProductColorImageService productColorImageService;
    @Resource
    private ProductSkuService productSkuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addProduct(ProductDTO productDTO) {
        ProductDO product = new ProductDO();
        BeanUtils.copyProperties(productDTO, product);
        baseMapper.insert(product);


        String productId = product.getId();
        List<ProductColorDTO> colorList = productDTO.getColorList();
        if (colorList != null && !colorList.isEmpty()) {
            productColorImageService.saveProductColorImage(productId, colorList);
        }

        List<String> sizeNameList = productDTO.getSizeNameList();
        if (colorList != null && !colorList.isEmpty()
                && sizeNameList != null && !sizeNameList.isEmpty()) {
            productSkuService.saveProductSku(productId, colorList, sizeNameList);
        }

        return productId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProduct(String productId, ProductDTO productDTO) {
        ProductDO product = baseMapper.selectById(productId);
        if (product == null || Objects.equals(product.getIsDelete(), GlobalEnums.YES.getCode())) {
            throw new BusinessException("更新失败，产品数据不存在。");
        }
        BeanUtils.copyProperties(productDTO, product, "id", "isDelete");
        baseMapper.updateById(product);


        List<ProductColorDTO> colorList = productDTO.getColorList();
        List<String> sizeNameList = productDTO.getSizeNameList();
        productSkuService.saveProductSku(productId, colorList, sizeNameList);
        productColorImageService.saveProductColorImage(productId, productDTO.getColorList());
        return true;
    }

    @Override
    public boolean deleteProduct(Long id, String userId) {
        return false;
    }

    @Override
    public ProductDetailRespDTO getProductById(String productId) {
        // 1. 查询商品主表
        ProductDO product = baseMapper.selectById(productId);
        if (product == null) {
            return null;
        }


        List<ProductColorImageDTO> colorImageList = productColorImageService.listProductColorImage(productId);
        List<ProductSkuDTO> skuList = productSkuService.listSkuWithStockByProductId(productId);

        ProductDetailRespDTO respDTO = new ProductDetailRespDTO();
        BeanUtils.copyProperties(product, respDTO);

        respDTO.setSkuList(skuList);
        respDTO.setProductColorImageList(colorImageList);

        return respDTO;
    }

    @Override
    public PageRespDTO<ProductPageRespDTO> pageProduct(ProductQueryParamDTO queryDTO) {
        Page<ProductPageRespDTO> page = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
        Page<ProductPageRespDTO> result = baseMapper.pageProduct(page, queryDTO);
        return PageRespDTO.of(result.getTotal(), result.getRecords(), queryDTO.getPage(), queryDTO.getLimit());
    }


}
