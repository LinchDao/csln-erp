package com.lin.csln.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.csln.dto.product.ProductPageRespDTO;
import com.lin.csln.dto.product.ProductQueryParamDTO;
import com.lin.csln.entity.ProductDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品表 Mapper接口
 *
 * @author 系统生成器
 */
@Mapper
public interface ProductMapper extends BaseMapper<ProductDO> {

    Page<ProductPageRespDTO> pageProduct(Page<ProductPageRespDTO> page, @Param("params") ProductQueryParamDTO queryDTO);
}
