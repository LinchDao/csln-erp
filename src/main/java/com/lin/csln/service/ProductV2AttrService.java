package com.lin.csln.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.dto.product.ProductV2AttrDTO;
import com.lin.csln.entity.ProductV2AttrDO;

import java.util.List;

public interface ProductV2AttrService extends IService<ProductV2AttrDO> {

    void saveProductV2Attrs(String productV2Id, List<ProductV2AttrDTO> attrList);

    List<ProductV2AttrDTO> listProductV2Attrs(String productV2Id);

    void softDeleteByProductV2Id(String productV2Id);
}
