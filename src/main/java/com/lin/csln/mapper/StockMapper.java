package com.lin.csln.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lin.csln.dto.stock.ProductStockPageRespDTO;
import com.lin.csln.dto.stock.ProductStockQueryParamDTO;
import com.lin.csln.entity.StockDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 库存表 Mapper接口
 * @author 系统生成器
 */
@Mapper
public interface StockMapper extends BaseMapper<StockDO> {

    IPage<ProductStockPageRespDTO> pageProductStock(IPage<ProductStockPageRespDTO> page,
                                                    @Param("params") ProductStockQueryParamDTO queryDTO);
}
