package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.dto.sys.menu.MenuDTO;
import com.lin.csln.dto.sys.menu.MenuListDTO;
import com.lin.csln.dto.sys.menu.MenuSortNodeDTO;
import com.lin.csln.entity.MenuDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.mapper.MenuMapper;
import com.lin.csln.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单表 服务实现类
 *
 * @author 系统生成器
 */
@Service
public class MenuServiceImpl extends BaseReadonlyServiceImpl<MenuMapper, MenuDO> implements MenuService {
    private static final Comparator<MenuDTO> MENU_SORT_COMPARATOR =
            Comparator.comparing(MenuDTO::getSort, Comparator.nullsLast(Integer::compareTo))
                    .thenComparing(MenuDTO::getId, Comparator.nullsLast(String::compareTo));

    @Override
    public List<MenuDTO> treeMenuByRoleCode(Boolean isAdmin, List<String> roles) {
        if (Boolean.TRUE.equals(isAdmin)) {
            List<MenuDO> menuDOList = lambdaQuery()
                    .eq(MenuDO::getStatus, GlobalEnums.YES.getCode())
                    .orderByAsc(MenuDO::getSort)
                    .list();
            List<MenuDTO> menuList = menuDOList.stream().map(this::toMenuDTO).collect(Collectors.toList());
            return buildTree(menuList);
        }
        List<MenuDTO> menuList = listMenuByRoleCode(roles);
        return buildTree(menuList);
    }

    @Override
    public List<MenuListDTO> listMenuForSelect() {
        List<MenuDO> menuList = lambdaQuery()
                .eq(MenuDO::getStatus, GlobalEnums.YES.getCode())
                .orderByAsc(MenuDO::getSort)
                .list();

        return menuList.stream().map(menu -> {
            MenuListDTO dto = new MenuListDTO();
            dto.setId(menu.getId());
            dto.setParentId(menu.getParentId());
            dto.setTitle(menu.getTitle());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMenuSort(List<MenuSortNodeDTO> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            throw new BusinessException("菜单树不能为空");
        }

        List<MenuDO> allMenus = lambdaQuery()
                .eq(MenuDO::getIsDelete, GlobalEnums.NO.getCode())
                .list();
        if (CollectionUtils.isEmpty(allMenus)) {
            throw new BusinessException("菜单不存在，无法保存排序");
        }

        Map<String, MenuDO> allMenuMap = allMenus.stream()
                .collect(Collectors.toMap(MenuDO::getId, menu -> menu));
        Map<String, MenuDO> enabledMenuMap = allMenus.stream()
                .filter(menu -> Objects.equals(menu.getStatus(), GlobalEnums.YES.getCode()))
                .collect(Collectors.toMap(MenuDO::getId, menu -> menu));
        if (enabledMenuMap.isEmpty()) {
            throw new BusinessException("启用菜单不存在，无法保存排序");
        }

        Set<String> requestIds = new HashSet<>();
        List<MenuDO> updateList = new ArrayList<>();
        collectSortUpdates(menus, null, enabledMenuMap, allMenuMap, requestIds, updateList);

        if (requestIds.size() != enabledMenuMap.size()) {
            throw new BusinessException("菜单树需全量提交");
        }

        this.updateBatchById(updateList);
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

    public List<MenuDTO> buildTree(List<MenuDTO> menuList) {
        if (menuList == null || menuList.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, MenuDTO> menuMap = menuList.stream()
                .collect(Collectors.toMap(MenuDTO::getId, menu -> menu));

        return menuList.stream()
                .filter(menu -> menu.getParentId() == null)
                .peek(menu -> menu.setChildren(getChildren(menu, menuMap)))
                .sorted(MENU_SORT_COMPARATOR)
                .collect(Collectors.toList());
    }

    private List<MenuDTO> getChildren(MenuDTO parent, Map<String, MenuDTO> menuMap) {
        return menuMap.values().stream()
                .filter(menu -> parent.getId().equals(menu.getParentId()))
                .peek(menu -> menu.setChildren(getChildren(menu, menuMap)))
                .sorted(MENU_SORT_COMPARATOR)
                .collect(Collectors.toList());
    }

    private MenuDTO toMenuDTO(MenuDO menuDO) {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setId(menuDO.getId());
        menuDTO.setParentId(menuDO.getParentId());
        menuDTO.setPath(menuDO.getPath());
        menuDTO.setComponent(menuDO.getComponent());
        menuDTO.setRedirect(menuDO.getRedirect());
        menuDTO.setName(menuDO.getName());
        menuDTO.setTitle(menuDO.getTitle());
        menuDTO.setIcon(menuDO.getIcon());
        menuDTO.setSort(menuDO.getSort());
        menuDTO.setHidden(menuDO.getHidden());
        return menuDTO;
    }

    private void collectSortUpdates(List<MenuSortNodeDTO> nodes,
                                    String expectedParentId,
                                    Map<String, MenuDO> enabledMenuMap,
                                    Map<String, MenuDO> allMenuMap,
                                    Set<String> requestIds,
                                    List<MenuDO> updateList) {
        if (CollectionUtils.isEmpty(nodes)) {
            return;
        }

        for (int i = 0; i < nodes.size(); i++) {
            MenuSortNodeDTO node = nodes.get(i);
            if (node == null || !StringUtils.hasText(node.getId())) {
                throw new BusinessException("菜单ID不能为空");
            }
            String menuId = node.getId().trim();
            if (!requestIds.add(menuId)) {
                throw new BusinessException("菜单ID重复：" + menuId);
            }

            MenuDO dbMenu = enabledMenuMap.get(menuId);
            if (dbMenu == null) {
                MenuDO allMenu = allMenuMap.get(menuId);
                if (allMenu == null) {
                    throw new BusinessException("菜单不存在：" + menuId);
                }
                throw new BusinessException("菜单已禁用，不能参与排序：" + menuId);
            }

            String dbParentId = normalizeParentId(dbMenu.getParentId());
            String requestParentId = normalizeParentId(expectedParentId);
            if (!Objects.equals(dbParentId, requestParentId)) {
                throw new BusinessException("不支持跨层级调整，菜单ID：" + menuId);
            }

            MenuDO updateDO = new MenuDO();
            updateDO.setId(menuId);
            updateDO.setSort(i + 1);
            updateList.add(updateDO);

            collectSortUpdates(node.getChildren(), menuId, enabledMenuMap, allMenuMap, requestIds, updateList);
        }
    }

    private String normalizeParentId(String parentId) {
        return StringUtils.hasText(parentId) ? parentId.trim() : null;
    }
}
