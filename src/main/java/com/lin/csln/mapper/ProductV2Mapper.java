package com.lin.csln.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lin.csln.dto.product.ProductPageV2RespDTO;
import com.lin.csln.dto.product.ProductV2QueryParamDTO;
import com.lin.csln.entity.ProductV2DO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductV2Mapper extends BaseMapper<ProductV2DO> {

    IPage<ProductPageV2RespDTO> pageProductV2(IPage<ProductPageV2RespDTO> page, @Param("params") ProductV2QueryParamDTO queryDTO);
}
