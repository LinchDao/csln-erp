package com.lin.csln.log.snapshot.impl;

import com.lin.csln.log.snapshot.OperationSnapshotProvider;
import com.lin.csln.enums.OperationLogModuleEnum;
import com.lin.csln.service.OrderMasterService;
import com.lin.csln.service.OrderSubService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 订单快照读取器（主单/子单）
 */
@Slf4j
@Component
public class OrderSnapshotProvider implements OperationSnapshotProvider {

    @Resource
    private OrderMasterService orderMasterService;
    @Resource
    private OrderSubService orderSubService;

    @Override
    public boolean supports(OperationLogModuleEnum module) {
        return OperationLogModuleEnum.ORDER_MASTER == module || OperationLogModuleEnum.ORDER_SUB == module;
    }

    @Override
    public Object snapshot(String bizId) {
        if (!StringUtils.hasText(bizId)) {
            return null;
        }

        // 先按主单读取，读不到再按子单读取，兼容同一Provider支持两个模块。
        try {
            return orderMasterService.getOrderMasterDetail(bizId);
        } catch (Exception e) {
            log.debug("读取订单主单快照失败，尝试子单快照。bizId={}", bizId, e);
        }

        try {
            return orderSubService.getDetail(bizId);
        } catch (Exception e) {
            log.warn("读取订单快照失败，bizId={}", bizId, e);
            return null;
        }
    }
}

