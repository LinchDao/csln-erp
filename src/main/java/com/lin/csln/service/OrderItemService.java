package com.lin.csln.service;

import com.lin.csln.entity.OrderItemDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.dto.order.OrderItemDTO;

import java.util.List;

/**
 * 订单明细表 服务接口
 * @author 系统生成器
 */
public interface OrderItemService extends IService<OrderItemDO> {

    List<OrderItemDO> listBySubId(String subId);

    void saveItems(String masterId, String subId, List<OrderItemDTO> items, Integer isDraft);

    void restoreLockQtyWhenDeleteSubOrder(String subId, String warehouseId);

    void lockDraftItemsByMasterId(String masterId);

}
