package com.lin.csln.service;

import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.dto.order.OrderSubDetailDTO;
import com.lin.csln.dto.order.OrderSubDetailRespDTO;
import com.lin.csln.dto.order.OrderSubDTO;
import com.lin.csln.dto.order.OrderSubPickingCompleteDTO;
import com.lin.csln.dto.order.OrderSubPageRespDTO;
import com.lin.csln.dto.order.OrderSubQueryParamDTO;
import com.lin.csln.dto.order.OrderSubShipDTO;
import com.lin.csln.entity.OrderSubDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 子订单表 服务接口
 * @author 系统生成器
 */
public interface OrderSubService extends IService<OrderSubDO> {

    void saveSubOrder(String masterId, String orderNo, List<OrderSubDTO> subOrders, Integer isDraft);

    List<OrderSubDetailRespDTO> listDetailByMasterId(String masterId);

    PageRespDTO<OrderSubPageRespDTO> pageQuery(OrderSubQueryParamDTO queryDTO);

    void assignOrderToUser(String orderSubId, String userId);

    OrderSubDetailDTO getDetail(String subId);

    void completePicking(OrderSubPickingCompleteDTO dto);

    void ship(OrderSubShipDTO dto);

    boolean hasFinishedOrShippedSubOrder(String masterId);
}
