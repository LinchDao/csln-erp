package com.lin.csln.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.dto.purchase.in.*;
import com.lin.csln.entity.PurchaseInDO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 采购入库单 服务接口
 *
 * @author 系统生成器
 */
public interface PurchaseInService extends IService<PurchaseInDO> {


    String createPurchaseIn(@Valid PurchaseInDTO dto, String userId);

    List<PurchaseInstockedQtyDTO> listInstockedQty(String purchaseId);

    Boolean checkExistUnAudit(String purchaseId);

    PageRespDTO<PurchaseInPageRespDTO> pagePurchaseIn(PurchaseInQueryParamDTO dto);

    PurchaseInDetailRespDTO getPurchaseInDetail(String inId);

    void auditPass(String inId, String userId);

    void auditReject(String inId, String userId);
}
