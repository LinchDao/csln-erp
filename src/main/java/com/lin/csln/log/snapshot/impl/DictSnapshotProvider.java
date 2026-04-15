package com.lin.csln.log.snapshot.impl;

import com.lin.csln.enums.OperationLogModuleEnum;
import com.lin.csln.log.snapshot.OperationSnapshotProvider;
import com.lin.csln.service.DictService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 字典快照读取器
 */
@Slf4j
@Component
public class DictSnapshotProvider implements OperationSnapshotProvider {

    @Resource
    private DictService dictService;

    @Override
    public boolean supports(OperationLogModuleEnum module) {
        return OperationLogModuleEnum.DICT == module;
    }

    @Override
    public Object snapshot(String bizId) {
        if (!StringUtils.hasText(bizId)) {
            return null;
        }
        try {
            return dictService.getDictById(bizId);
        } catch (Exception e) {
            log.warn("读取字典快照失败，bizId={}", bizId, e);
            return null;
        }
    }
}

