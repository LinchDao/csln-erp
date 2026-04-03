package com.lin.csln.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lin.csln.dto.order.OrderSubDetailDTO;
import com.lin.csln.dto.order.OrderSubDetailRespDTO;
import com.lin.csln.dto.order.OrderSubPageRespDTO;
import com.lin.csln.dto.order.OrderSubQueryParamDTO;
import com.lin.csln.entity.OrderSubDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 子订单表 Mapper接口
 * @author 系统生成器
 */
@Mapper
public interface OrderSubMapper extends BaseMapper<OrderSubDO> {

    String selectMaxSubOrderNoByMasterIdAndPrefix(@Param("masterId") String masterId,
                                                  @Param("subOrderPrefix") String subOrderPrefix);

    List<OrderSubDetailRespDTO> selectDetailListByMasterId(@Param("masterId") String masterId);

    OrderSubDetailDTO selectDetailBySubId(@Param("subId") String subId);

    IPage<OrderSubPageRespDTO> pageQuery(IPage<OrderSubPageRespDTO> page,
                                         @Param("query") OrderSubQueryParamDTO query);
}
