package com.lin.csln.log.snapshot;

import com.lin.csln.enums.OperationLogModuleEnum;

/**
 * 业务快照读取器
 */
public interface OperationSnapshotProvider {

    boolean supports(OperationLogModuleEnum module);

    Object snapshot(String bizId);
}

