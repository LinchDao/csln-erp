package com.lin.csln.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.entity.RoleMenuDO;

import java.util.List;

public interface RoleMenuService extends IService<RoleMenuDO> {

    List<String> listMenuIdsByRoleId(String roleId);

    void replaceMenus(String roleId, List<String> menuIds);
}
