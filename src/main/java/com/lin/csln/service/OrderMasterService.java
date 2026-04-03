package com.lin.csln.service;

import com.lin.csln.entity.OrderMasterDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.dto.order.OrderMasterDetailRespDTO;
import com.lin.csln.dto.order.OrderMasterDTO;
import com.lin.csln.dto.order.OrderMasterPageRespDTO;
import com.lin.csln.dto.order.OrderMasterQueryParamDTO;

/**
 * 订单主表 服务接口
 * @author 系统生成器
 */
public interface OrderMasterService extends IService<OrderMasterDO> {

    PageRespDTO<OrderMasterPageRespDTO> pageOrderMaster(OrderMasterQueryParamDTO queryParamDTO);

    OrderMasterDetailRespDTO getOrderMasterDetail(String id);

    String createOrderMaster(OrderMasterDTO dto, String userId);

    void editOrderMaster(String id, OrderMasterDTO dto);

    void editDraftOrderMaster(String id, OrderMasterDTO dto);

    void submitOrderMaster(String id, OrderMasterDTO dto);

    void syncStatusAfterSubShipped(String masterId, long totalSubCount, long shippedSubCount);
}
