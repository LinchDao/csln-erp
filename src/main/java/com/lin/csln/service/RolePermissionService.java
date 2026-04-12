package com.lin.csln.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.entity.RolePermissionDO;

import java.util.List;

public interface RolePermissionService extends IService<RolePermissionDO> {

    List<String> listPermIdsByRoleIds(List<String> roleIds);

    List<String> listPermIdsByRoleId(String roleId);

    void replacePermissions(String roleId, List<String> permIds);
}
