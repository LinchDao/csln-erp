package com.lin.csln.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.dto.product.ProductDTO;
import com.lin.csln.dto.product.ProductQueryDTO;
import com.lin.csln.entity.ProductDO;
import jakarta.validation.Valid;

/**
 * 商品表 服务接口
 *
 * @author 系统生成器
 */
public interface ProductService extends IService<ProductDO> {

    Long saveProduct(ProductDTO productDTO);

    boolean updateProduct(@Valid ProductDTO productDTO);

    boolean deleteProduct(Long id);

    ProductDTO getProductById(Long id);

    IPage<ProductDTO> pageProduct(Page<ProductDTO> page, ProductQueryDTO queryDTO);
}
