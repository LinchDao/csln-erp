package com.lin.csln.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.dto.product.ProductDTO;
import com.lin.csln.dto.product.ProductPageRespDTO;
import com.lin.csln.dto.product.ProductQueryParamDTO;
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

    boolean deleteProduct(Long id, String userId);

    ProductDTO getProductById(Long id);

    PageRespDTO<ProductPageRespDTO> pageProduct(ProductQueryParamDTO queryDTO);
}
