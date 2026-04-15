package com.lin.csln.service.impl;

import com.lin.csln.service.PermissionService;
import com.lin.csln.service.RolePermissionService;
import com.lin.csln.service.UserPermissionService;
import com.lin.csln.service.UserRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserPermissionServiceImpl implements UserPermissionService {

    @Resource
    private UserRoleService userRoleService;
    @Resource
    private RolePermissionService rolePermissionService;
    @Resource
    private PermissionService permissionService;

    @Override
    public Set<String> listPermCodesByUserId(String userId) {
        if (!StringUtils.hasText(userId)) {
            return Collections.emptySet();
        }

        List<String> roleIds = userRoleService.listRoleIdsByUserId(userId);
        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptySet();
        }

        List<String> permIds = rolePermissionService.listPermIdsByRoleIds(roleIds);
        if (permIds == null || permIds.isEmpty()) {
            return Collections.emptySet();
        }

        List<String> permCodes = permissionService.listPermCodesByPermIds(permIds);
        if (permCodes == null || permCodes.isEmpty()) {
            return Collections.emptySet();
        }

        return new LinkedHashSet<>(permCodes);
    }
}
