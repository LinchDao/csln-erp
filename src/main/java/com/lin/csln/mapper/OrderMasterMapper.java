package com.lin.csln.mapper;

import com.lin.csln.entity.OrderMasterDO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lin.csln.dto.order.OrderMasterPageRespDTO;
import com.lin.csln.dto.order.OrderMasterQueryParamDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单主表 Mapper接口
 * @author 系统生成器
 */
@Mapper
public interface OrderMasterMapper extends BaseMapper<OrderMasterDO> {

    IPage<OrderMasterPageRespDTO> pageOrderMaster(IPage<OrderMasterPageRespDTO> page,
                                                  @Param("query") OrderMasterQueryParamDTO query);

    String selectMaxOrderNoByPrefix(@Param("orderNoPrefix") String orderNoPrefix);
}
