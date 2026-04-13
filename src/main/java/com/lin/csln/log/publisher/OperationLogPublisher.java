package com.lin.csln.log.publisher;

import com.lin.csln.entity.LogDO;

/**
 * 操作日志发布器
 */
public interface OperationLogPublisher {

    void publish(LogDO logDO, boolean requireNewTx);
}

