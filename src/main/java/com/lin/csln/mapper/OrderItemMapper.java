package com.lin.csln.mapper;

import com.lin.csln.dto.order.OrderItemDetailRespDTO;
import com.lin.csln.entity.OrderItemDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单明细表 Mapper接口
 * @author 系统生成器
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItemDO> {

    List<OrderItemDetailRespDTO> selectDetailListByMasterId(@Param("masterId") String masterId);

}
