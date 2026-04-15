package com.lin.csln.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.entity.UserRoleDO;

import java.util.List;

public interface UserRoleService extends IService<UserRoleDO> {

    List<String> listRoleIdsByUserId(String userId);

    void replaceRoles(String userId, List<String> roleIds);

    long countByRoleId(String roleId);
}
