package com.lin.csln.service;

import com.lin.csln.dto.product.ProductColorDTO;
import com.lin.csln.dto.product.ProductColorImageDTO;
import com.lin.csln.entity.ProductColorImageDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 商品颜色图片表 服务接口
 * @author 系统生成器
 */
public interface ProductColorImageService extends IService<ProductColorImageDO> {

    List<ProductColorImageDTO> listProductColorImage(String productId);

    void saveProductColorImage(String productId, List<ProductColorDTO> colorList);
}
