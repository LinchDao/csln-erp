package com.lin.csln.log.snapshot.impl;

import com.lin.csln.log.snapshot.OperationSnapshotProvider;
import com.lin.csln.enums.OperationLogModuleEnum;
import com.lin.csln.service.PurchaseInService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 采购入库单快照读取器
 */
@Slf4j
@Component
public class PurchaseInSnapshotProvider implements OperationSnapshotProvider {

    @Resource
    private PurchaseInService purchaseInService;

    @Override
    public boolean supports(OperationLogModuleEnum module) {
        return OperationLogModuleEnum.PURCHASE_IN == module;
    }

    @Override
    public Object snapshot(String bizId) {
        if (!StringUtils.hasText(bizId)) {
            return null;
        }
        try {
            return purchaseInService.getPurchaseInDetail(bizId);
        } catch (Exception e) {
            log.warn("读取采购入库单快照失败，bizId={}", bizId, e);
            return null;
        }
    }
}

