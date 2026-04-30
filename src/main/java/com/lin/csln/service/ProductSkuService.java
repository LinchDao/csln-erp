package com.lin.csln.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.dto.product.ProductSkuListDTO;
import com.lin.csln.dto.product.ProductColorDTO;
import com.lin.csln.dto.product.ProductSkuDTO;
import com.lin.csln.dto.product.ProductSkuV2RespDTO;
import com.lin.csln.dto.product.ProductSkuV2DTO;
import com.lin.csln.entity.ProductSkuDO;

import java.util.List;

/**
 * SKU表 服务接口
 *
 * @author 系统生成器
 */
public interface ProductSkuService extends IService<ProductSkuDO> {

    List<ProductSkuDTO> listSkuWithStockByProductId(String productId);


    void saveProductSku(String productId, List<ProductColorDTO> colorList, List<String> sizeNameList);

    List<ProductSkuListDTO> getSkuByProductId(String productId);

    void saveProductSkuV2(String productId, List<ProductSkuV2DTO> skuList, List<String> mountDimKeys);

    List<ProductSkuV2RespDTO> listSkuWithDimsByProductId(String productId);

    List<String> listMountDimKeysByProductId(String productId);

    boolean hasOccupiedOrReferencedSkuIds(List<String> skuIds);
}
