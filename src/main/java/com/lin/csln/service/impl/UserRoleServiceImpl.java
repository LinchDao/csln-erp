package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.csln.entity.UserRoleDO;
import com.lin.csln.mapper.UserRoleMapper;
import com.lin.csln.service.UserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl extends BaseReadonlyServiceImpl<UserRoleMapper, UserRoleDO> implements UserRoleService {

    @Override
    public List<String> listRoleIdsByUserId(String userId) {
        if (!StringUtils.hasText(userId)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<UserRoleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRoleDO::getUserId, userId);
        wrapper.select(UserRoleDO::getRoleId);
        List<UserRoleDO> userRoleList = this.list(wrapper);
        return userRoleList.stream()
                .map(UserRoleDO::getRoleId)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replaceRoles(String userId, List<String> roleIds) {
        LambdaQueryWrapper<UserRoleDO> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(UserRoleDO::getUserId, userId);
        this.remove(deleteWrapper);

        if (roleIds == null || roleIds.isEmpty()) {
            return;
        }

        Set<String> uniqueRoleIds = new LinkedHashSet<>(roleIds);
        List<UserRoleDO> saveList = new ArrayList<>();
        for (String roleId : uniqueRoleIds) {
            if (!StringUtils.hasText(roleId)) {
                continue;
            }
            UserRoleDO userRole = new UserRoleDO();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            saveList.add(userRole);
        }

        if (!saveList.isEmpty()) {
            this.saveBatch(saveList);
        }
    }
}