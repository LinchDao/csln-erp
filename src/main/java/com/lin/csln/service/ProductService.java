package com.lin.csln.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.dto.product.ProductDTO;
import com.lin.csln.dto.product.ProductDetailRespDTO;
import com.lin.csln.dto.product.ProductPageRespDTO;
import com.lin.csln.dto.product.ProductQueryParamDTO;
import com.lin.csln.entity.ProductDO;

/**
 * 商品表 服务接口
 *
 * @author 系统生成器
 */
public interface ProductService extends IService<ProductDO> {

    String addProduct(ProductDTO productDTO);

    boolean updateProduct(String productId, ProductDTO productDTO);

    boolean deleteProduct(Long id, String userId);

    ProductDetailRespDTO getProductById(String id);

    PageRespDTO<ProductPageRespDTO> pageProduct(ProductQueryParamDTO queryDTO);

}
