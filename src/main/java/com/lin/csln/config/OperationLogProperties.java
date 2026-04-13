package com.lin.csln.config;

import com.lin.csln.enums.OperationLogRouteEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 操作日志配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "csln.log")
public class OperationLogProperties {

    /**
     * 日志路由，默认同步写库
     */
    private OperationLogRouteEnum route = OperationLogRouteEnum.SYNC;

    /**
     * MQ 配置
     */
    private Mq mq = new Mq();

    @Data
    public static class Mq {
        private String topic = "erp_operation_log";
        private String tag = "OP_LOG";
        private String consumerGroup = "csln-erp-log-consumer";
    }
}
