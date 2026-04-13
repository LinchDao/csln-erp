package com.lin.csln.log.publisher;

import com.lin.csln.config.OperationLogProperties;
import com.lin.csln.entity.LogDO;
import com.lin.csln.enums.OperationLogRouteEnum;
import com.lin.csln.service.LogService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 可切换操作日志发布器
 */
@Slf4j
@Component
public class SwitchableOperationLogPublisher implements OperationLogPublisher {

    @Resource
    private OperationLogProperties operationLogProperties;
    @Resource
    private LogService logService;

    @Override
    public void publish(LogDO logDO, boolean requireNewTx) {
        if (logDO == null) {
            return;
        }
        if (OperationLogRouteEnum.MQ == operationLogProperties.getRoute()) {
            publishToMq(logDO, requireNewTx);
            return;
        }
        publishSync(logDO, requireNewTx);
    }

    private void publishSync(LogDO logDO, boolean requireNewTx) {
        if (requireNewTx) {
            logService.saveFailLog(logDO);
            return;
        }
        logService.saveLog(logDO);
    }

    /**
     * MQ链路预留：当前先降级为同步写库
     */
    private void publishToMq(LogDO logDO, boolean requireNewTx) {
        log.info("操作日志路由为MQ，当前版本未接入MQ客户端，降级为同步写库。logId={}", logDO.getId());
        publishSync(logDO, requireNewTx);
    }
}

