package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.csln.entity.RoleMenuDO;
import com.lin.csln.mapper.RoleMenuMapper;
import com.lin.csln.service.RoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleMenuServiceImpl extends BaseReadonlyServiceImpl<RoleMenuMapper, RoleMenuDO> implements RoleMenuService {

    @Override
    public List<String> listMenuIdsByRoleId(String roleId) {
        if (!StringUtils.hasText(roleId)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<RoleMenuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenuDO::getRoleId, roleId);
        wrapper.select(RoleMenuDO::getMenuId);
        List<RoleMenuDO> roleMenuList = this.list(wrapper);
        if (roleMenuList == null || roleMenuList.isEmpty()) {
            return new ArrayList<>();
        }
        return roleMenuList.stream()
                .map(RoleMenuDO::getMenuId)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replaceMenus(String roleId, List<String> menuIds) {
        LambdaQueryWrapper<RoleMenuDO> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(RoleMenuDO::getRoleId, roleId);
        this.remove(deleteWrapper);

        if (menuIds == null || menuIds.isEmpty()) {
            return;
        }

        Set<String> uniqueMenuIds = new LinkedHashSet<>(menuIds);
        List<RoleMenuDO> saveList = new ArrayList<>();
        for (String menuId : uniqueMenuIds) {
            if (!StringUtils.hasText(menuId)) {
                continue;
            }
            RoleMenuDO roleMenu = new RoleMenuDO();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            saveList.add(roleMenu);
        }

        if (!saveList.isEmpty()) {
            this.saveBatch(saveList);
        }
    }
}
