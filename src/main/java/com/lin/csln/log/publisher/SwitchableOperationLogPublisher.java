package com.lin.csln.log.publisher;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.lin.csln.config.OperationLogProperties;
import com.lin.csln.entity.LogDO;
import com.lin.csln.enums.OperationLogRouteEnum;
import com.lin.csln.service.LogService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
    @Resource
    private ObjectProvider<RocketMQTemplate> rocketMQTemplateProvider;

    @Override
    public void publish(LogDO logDO, boolean requireNewTx) {
        if (logDO == null) {
            return;
        }
        if (OperationLogRouteEnum.MQ == operationLogProperties.getRoute()) {
            publishToMq(logDO);
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
     * MQ 路由：发送失败仅记录告警，不影响主流程。
     */
    private void publishToMq(LogDO logDO) {
        RocketMQTemplate rocketMQTemplate = rocketMQTemplateProvider.getIfAvailable();
        if (rocketMQTemplate == null) {
            log.warn("操作日志路由为MQ，但RocketMQTemplate不可用，跳过发送。logId={}", logDO.getId());
            return;
        }

        ensureLogId(logDO);
        String destination = buildDestination();
        String payload = JSON.toJSONString(logDO);
        log.info("操作日志准备异步发送MQ。logId={}, destination={}", logDO.getId(), destination);
        Message<String> message = MessageBuilder.withPayload(payload)
                .setHeader(RocketMQHeaders.KEYS, logDO.getId())
                .build();
        try {
            rocketMQTemplate.asyncSend(destination, message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.debug("操作日志异步发送MQ成功。logId={}, destination={}, sendResult={}",
                            logDO.getId(), destination, sendResult);
                }

                @Override
                public void onException(Throwable throwable) {
                    log.warn("操作日志异步发送MQ失败。logId={}, destination={}",
                            logDO.getId(), destination, throwable);
                }
            });
        } catch (Exception ex) {
            log.warn("操作日志异步发送MQ异常，已按策略忽略。logId={}, destination={}",
                    logDO.getId(), destination, ex);
        }
    }

    private String buildDestination() {
        String topic = operationLogProperties.getMq().getTopic();
        String tag = operationLogProperties.getMq().getTag();
        if (!StringUtils.hasText(tag)) {
            return topic;
        }
        return topic + ":" + tag;
    }

    private void ensureLogId(LogDO logDO) {
        if (StringUtils.hasText(logDO.getId())) {
            return;
        }
        logDO.setId(IdUtil.getSnowflakeNextIdStr());
    }
}
