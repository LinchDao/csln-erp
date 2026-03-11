package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.csln.dto.sys.menu.MenuDTO;
import com.lin.csln.entity.MenuDO;
import com.lin.csln.mapper.MenuMapper;
import com.lin.csln.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单表 服务实现类
 *
 * @author 系统生成器
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuDO> implements MenuService {

    @Override
    public List<MenuDTO> treeMenuByRoleCode(List<String> roles) {
        List<MenuDTO> menuList = listMenuByRoleCode(roles);
        return buildTree(menuList);
    }

    public List<MenuDTO> listMenuByRoleCode(List<String> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.emptyList();
        }
        List<MenuDTO> menuList = baseMapper.selectMenusByRoleCodes(roles);
        if (CollectionUtils.isEmpty(menuList)) {
            return new ArrayList<>();
        }
        return menuList;
    }

    /**
     * 构建菜单树
     */
    public List<MenuDTO> buildTree(List<MenuDTO> menuList) {
        if (menuList == null || menuList.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Long, MenuDTO> menuMap = menuList.stream()
                .collect(Collectors.toMap(MenuDTO::getId, menu -> menu));

        return menuList.stream()
                .filter(menu -> menu.getParentId() == null || Objects.equals(menu.getParentId(), 0L))
                .peek(menu -> menu.setChildren(getChildren(menu, menuMap)))
                .sorted(Comparator.comparing(MenuDTO::getSort,
                                Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(MenuDTO::getId, Comparator.nullsLast(Long::compareTo)))
                .collect(Collectors.toList());
    }

    /**
     * 递归获取子节点
     */
    private List<MenuDTO> getChildren(MenuDTO parent, Map<Long, MenuDTO> menuMap) {
        return menuMap.values().stream()
                .filter(menu -> parent.getId().equals(menu.getParentId()))
                .peek(menu -> menu.setChildren(getChildren(menu, menuMap)))
                .sorted(Comparator.comparing(MenuDTO::getSort,
                                Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(MenuDTO::getId, Comparator.nullsLast(Long::compareTo)))
                .collect(Collectors.toList());
    }
}
