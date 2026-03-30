package com.lin.csln.service;

import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.dto.purchase.PurchaseOrderDTO;
import com.lin.csln.dto.purchase.PurchaseOrderPageRespDTO;
import com.lin.csln.dto.purchase.PurchaseOrderQueryParamDTO;
import com.lin.csln.entity.PurchaseOrderDO;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.Valid;

/**
 * 采购订单 服务接口
 * @author 系统生成器
 */
public interface PurchaseOrderService extends IService<PurchaseOrderDO> {

    PageRespDTO<PurchaseOrderPageRespDTO> pagePurchaseOrder(PurchaseOrderQueryParamDTO queryDTO);

    String createPurchaseOrder(PurchaseOrderDTO dto, String userId);

    PurchaseOrderDTO getPurchaseDetail(String id);

    void editPurchaseOrder(String id, PurchaseOrderDTO dto);

    void cancelPurchaseOrder(String id);
}
