package com.lin.csln.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.dto.product.ProductDetailV2RespDTO;
import com.lin.csln.dto.product.ProductPageV2RespDTO;
import com.lin.csln.dto.product.ProductV2DTO;
import com.lin.csln.dto.product.ProductV2QueryParamDTO;
import com.lin.csln.entity.ProductV2DO;

public interface ProductV2Service extends IService<ProductV2DO> {

    String addProductV2(ProductV2DTO productDTO);

    boolean updateProductV2(String productId, ProductV2DTO productDTO);

    ProductDetailV2RespDTO getProductByIdV2(String productId);

    PageRespDTO<ProductPageV2RespDTO> pageProductV2(ProductV2QueryParamDTO queryDTO);

    boolean deleteProductV2(String productId);
}
