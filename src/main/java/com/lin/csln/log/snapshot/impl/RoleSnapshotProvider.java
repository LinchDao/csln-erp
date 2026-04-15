package com.lin.csln.log.snapshot.impl;

import com.lin.csln.enums.OperationLogModuleEnum;
import com.lin.csln.log.snapshot.OperationSnapshotProvider;
import com.lin.csln.service.RoleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 角色快照读取器
 */
@Slf4j
@Component
public class RoleSnapshotProvider implements OperationSnapshotProvider {

    @Resource
    private RoleService roleService;

    @Override
    public boolean supports(OperationLogModuleEnum module) {
        return OperationLogModuleEnum.ROLE == module;
    }

    @Override
    public Object snapshot(String bizId) {
        if (!StringUtils.hasText(bizId)) {
            return null;
        }
        try {
            return roleService.getRoleDetail(bizId);
        } catch (Exception e) {
            log.warn("读取角色快照失败，bizId={}", bizId, e);
            return null;
        }
    }
}

