package com.lin.csln.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.dto.purchase.order.PurchaseOrderDTO;
import com.lin.csln.dto.purchase.order.PurchaseOrderPageRespDTO;
import com.lin.csln.dto.purchase.order.PurchaseOrderQueryParamDTO;
import com.lin.csln.entity.PurchaseOrderDO;

/**
 * 采购订单 服务接口
 *
 * @author 系统生成器
 */
public interface PurchaseOrderService extends IService<PurchaseOrderDO> {

    PageRespDTO<PurchaseOrderPageRespDTO> pagePurchaseOrder(PurchaseOrderQueryParamDTO queryDTO);

    String createPurchaseOrder(PurchaseOrderDTO dto, String userId);

    PurchaseOrderDTO getPurchaseDetail(String id);

    void editPurchaseOrder(String id, PurchaseOrderDTO dto);

    void cancelPurchaseOrder(String id);

    void updatePurchaseOrderStatus(String purchaseId);
}
