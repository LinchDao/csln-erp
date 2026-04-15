package com.lin.csln.log.consumer;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.lin.csln.entity.LogDO;
import com.lin.csln.service.LogService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 操作日志 MQ 消费者
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "csln.log", name = "route", havingValue = "MQ")
@RocketMQMessageListener(
        topic = "${csln.log.mq.topic:erp_operation_log}",
        selectorExpression = "${csln.log.mq.tag:OP_LOG}",
        consumerGroup = "${csln.log.mq.consumer-group:csln-erp-log-consumer}"
)
public class OperationLogMqConsumer implements RocketMQListener<String> {

    @Resource
    private LogService logService;

    //自消费方案，MQ示例
    @Override
    public void onMessage(String message) {
        if (!StringUtils.hasText(message)) {
            return;
        }
        try {
            LogDO logDO = JSON.parseObject(message, LogDO.class);
            if (logDO == null) {
                return;
            }
            if (!StringUtils.hasText(logDO.getId())) {
                logDO.setId(IdUtil.getSnowflakeNextIdStr());
            }
            logService.saveLogIgnoreDuplicate(logDO);
        } catch (Exception ex) {
            log.error("消费操作日志消息失败，将由MQ重试。message={}", message, ex);
            throw new IllegalStateException("consume operation log message failed", ex);
        }
    }
}
