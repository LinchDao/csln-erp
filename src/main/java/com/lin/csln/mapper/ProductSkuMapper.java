package com.lin.csln.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.csln.dto.product.ProductSkuDTO;
import com.lin.csln.entity.ProductSkuDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SKU表 Mapper接口
 *
 * @author 系统生成器
 */
@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSkuDO> {
    List<ProductSkuDTO> listSkuWithStockByProductId(@Param("productId") String productId);

}
