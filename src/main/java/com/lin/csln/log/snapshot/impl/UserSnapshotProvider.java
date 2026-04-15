package com.lin.csln.log.snapshot.impl;

import com.lin.csln.enums.OperationLogModuleEnum;
import com.lin.csln.log.snapshot.OperationSnapshotProvider;
import com.lin.csln.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 用户快照读取器
 */
@Slf4j
@Component
public class UserSnapshotProvider implements OperationSnapshotProvider {

    @Resource
    private UserService userService;

    @Override
    public boolean supports(OperationLogModuleEnum module) {
        return OperationLogModuleEnum.USER == module;
    }

    @Override
    public Object snapshot(String bizId) {
        if (!StringUtils.hasText(bizId)) {
            return null;
        }
        try {
            return userService.getUserDetail(bizId);
        } catch (Exception e) {
            log.warn("读取用户快照失败，bizId={}", bizId, e);
            return null;
        }
    }
}

