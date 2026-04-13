package com.lin.csln.log.snapshot.impl;

import com.lin.csln.log.snapshot.OperationSnapshotProvider;
import com.lin.csln.enums.OperationLogModuleEnum;
import com.lin.csln.service.PurchaseOrderService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 采购单快照读取器
 */
@Slf4j
@Component
public class PurchaseOrderSnapshotProvider implements OperationSnapshotProvider {

    @Resource
    private PurchaseOrderService purchaseOrderService;

    @Override
    public boolean supports(OperationLogModuleEnum module) {
        return OperationLogModuleEnum.PURCHASE_ORDER == module;
    }

    @Override
    public Object snapshot(String bizId) {
        if (!StringUtils.hasText(bizId)) {
            return null;
        }
        try {
            return purchaseOrderService.getPurchaseDetail(bizId);
        } catch (Exception e) {
            log.warn("读取采购单快照失败，bizId={}", bizId, e);
            return null;
        }
    }
}

