package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.csln.entity.RolePermissionDO;
import com.lin.csln.mapper.RolePermissionMapper;
import com.lin.csln.service.RolePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RolePermissionServiceImpl extends BaseReadonlyServiceImpl<RolePermissionMapper, RolePermissionDO> implements RolePermissionService {

    @Override
    public List<String> listPermIdsByRoleIds(List<String> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<RolePermissionDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(RolePermissionDO::getRoleId, roleIds);
        wrapper.select(RolePermissionDO::getPermId);
        List<RolePermissionDO> rolePermissionList = this.list(wrapper);
        if (rolePermissionList == null || rolePermissionList.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> permIdSet = rolePermissionList.stream()
                .map(RolePermissionDO::getPermId)
                .filter(StringUtils::hasText)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return new ArrayList<>(permIdSet);
    }

    @Override
    public List<String> listPermIdsByRoleId(String roleId) {
        if (!StringUtils.hasText(roleId)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<RolePermissionDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermissionDO::getRoleId, roleId);
        wrapper.select(RolePermissionDO::getPermId);
        List<RolePermissionDO> rolePermissionList = this.list(wrapper);
        if (rolePermissionList == null || rolePermissionList.isEmpty()) {
            return new ArrayList<>();
        }

        return rolePermissionList.stream()
                .map(RolePermissionDO::getPermId)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replacePermissions(String roleId, List<String> permIds) {
        LambdaQueryWrapper<RolePermissionDO> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(RolePermissionDO::getRoleId, roleId);
        this.remove(deleteWrapper);

        if (permIds == null || permIds.isEmpty()) {
            return;
        }

        Set<String> uniquePermIds = new LinkedHashSet<>(permIds);
        List<RolePermissionDO> saveList = new ArrayList<>();
        for (String permId : uniquePermIds) {
            if (!StringUtils.hasText(permId)) {
                continue;
            }
            RolePermissionDO rolePermission = new RolePermissionDO();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermId(permId);
            saveList.add(rolePermission);
        }

        if (!saveList.isEmpty()) {
            this.saveBatch(saveList);
        }
    }
}
