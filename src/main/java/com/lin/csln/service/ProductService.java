package com.lin.csln.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.dto.product.*;
import com.lin.csln.entity.ProductDO;

import java.util.List;

/**
 * 商品表 服务接口
 *
 * @author 系统生成器
 */
public interface ProductService extends IService<ProductDO> {

    String addProduct(ProductDTO productDTO);

    boolean updateProduct(String productId, ProductDTO productDTO);

    boolean deleteProduct(String id, String userId);

    ProductDetailRespDTO getProductById(String id);

    PageRespDTO<ProductPageRespDTO> pageProduct(ProductQueryParamDTO queryDTO);

    List<ProductSelectDTO> listProductSelect(String productNo);
}
