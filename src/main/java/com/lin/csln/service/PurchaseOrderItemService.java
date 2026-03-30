package com.lin.csln.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.dto.purchase.PurchaseOrderItemDTO;
import com.lin.csln.entity.PurchaseOrderItemDO;

import java.util.List;

/**
 * 采购明细表 服务接口
 *
 * @author 系统生成器
 */
public interface PurchaseOrderItemService extends IService<PurchaseOrderItemDO> {

    void savePurchaseOrderItem(String orderId, List<PurchaseOrderItemDTO> purchaseOrderItem);

    List<PurchaseOrderItemDTO> listPurchaseOrderItemSku(String purchaseId);
}
