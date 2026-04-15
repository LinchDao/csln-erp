package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.dto.sys.role.RoleDetailRespDTO;
import com.lin.csln.dto.sys.role.RoleListDTO;
import com.lin.csln.dto.sys.role.RolePageRespDTO;
import com.lin.csln.dto.sys.role.RoleQueryParamDTO;
import com.lin.csln.dto.sys.role.RoleSaveReqDTO;
import com.lin.csln.entity.MenuDO;
import com.lin.csln.entity.PermissionDO;
import com.lin.csln.entity.RoleDO;
import com.lin.csln.enums.RoleEnums;
import com.lin.csln.mapper.RoleMapper;
import com.lin.csln.service.MenuService;
import com.lin.csln.service.PermissionService;
import com.lin.csln.service.RoleMenuService;
import com.lin.csln.service.RolePermissionService;
import com.lin.csln.service.RoleService;
import com.lin.csln.service.UserRoleService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends BaseReadonlyServiceImpl<RoleMapper, RoleDO> implements RoleService {

    @Resource
    private RoleMenuService roleMenuService;
    @Resource
    private RolePermissionService rolePermissionService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private MenuService menuService;
    @Resource
    private PermissionService permissionService;

    @Override
    public List<RoleDO> listRolesByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return Collections.emptyList();
        }

        try {
            List<RoleDO> roles = baseMapper.listRolesByUserId(userId);
            return roles != null ? roles : Collections.emptyList();

        } catch (Exception e) {

            return Collections.emptyList();
        }
    }

    @Override
    public List<RoleListDTO> listRoleForSelect() {
        LambdaQueryWrapper<RoleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(RoleDO::getId, RoleDO::getRoleCode, RoleDO::getRoleName);
        wrapper.orderByAsc(RoleDO::getRoleName);
        wrapper.ne(RoleDO::getRoleCode, RoleEnums.SUPER_ADMIN.getCode());

        List<RoleDO> roleList = baseMapper.selectList(wrapper);
        return roleList.stream().map(role -> {
            RoleListDTO dto = new RoleListDTO();
            dto.setId(role.getId());
            dto.setRoleCode(role.getRoleCode());
            dto.setRoleName(role.getRoleName());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public PageRespDTO<RolePageRespDTO> pageRole(RoleQueryParamDTO queryDTO) {
        RoleQueryParamDTO query = queryDTO == null ? new RoleQueryParamDTO() : queryDTO;
        int pageNo = query.getPage() == null || query.getPage() < 1 ? 1 : query.getPage();
        int pageSize = query.getLimit() == null || query.getLimit() < 1 ? 10 : query.getLimit();

        IPage<RoleDO> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<RoleDO> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(query.getRoleCode())) {
            wrapper.like(RoleDO::getRoleCode, query.getRoleCode().trim());
        }
        if (StringUtils.isNotBlank(query.getRoleName())) {
            wrapper.like(RoleDO::getRoleName, query.getRoleName().trim());
        }
        wrapper.orderByAsc(RoleDO::getRoleName);

        IPage<RoleDO> resultPage = this.page(page, wrapper);
        List<RolePageRespDTO> rows = resultPage.getRecords().stream().map(this::toRolePageResp).collect(Collectors.toList());
        return PageRespDTO.of(resultPage.getTotal(), rows, pageNo, pageSize);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addRole(RoleSaveReqDTO dto) {
        validateSaveReq(dto, false);

        List<String> menuIds = normalizeIds(dto.getMenuIds());
        List<String> permIds = normalizeIds(dto.getPermIds());
        validateMenuIds(menuIds);
        validatePermIds(permIds);
        checkRoleCodeUnique(dto.getRoleCode(), null);

        RoleDO role = new RoleDO();
        role.setRoleCode(dto.getRoleCode().trim());
        role.setRoleName(dto.getRoleName().trim());
        role.setRemark(normalizeNullableText(dto.getRemark()));
        this.save(role);

        roleMenuService.replaceMenus(role.getId(), menuIds);
        rolePermissionService.replacePermissions(role.getId(), permIds);
        return role.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleSaveReqDTO dto) {
        validateSaveReq(dto, true);

        RoleDO dbRole = this.getById(dto.getId());
        if (dbRole == null) {
            throw new BusinessException("角色不存在");
        }
        ensureNotSuperAdmin(dbRole);

        List<String> menuIds = normalizeIds(dto.getMenuIds());
        List<String> permIds = normalizeIds(dto.getPermIds());
        validateMenuIds(menuIds);
        validatePermIds(permIds);
        checkRoleCodeUnique(dto.getRoleCode(), dto.getId());

        RoleDO updateRole = new RoleDO();
        updateRole.setId(dto.getId());
        updateRole.setRoleCode(dto.getRoleCode().trim());
        updateRole.setRoleName(dto.getRoleName().trim());
        updateRole.setRemark(normalizeNullableText(dto.getRemark()));
        this.updateById(updateRole);

        roleMenuService.replaceMenus(dto.getId(), menuIds);
        rolePermissionService.replacePermissions(dto.getId(), permIds);
    }

    @Override
    public RoleDetailRespDTO getRoleDetail(String id) {
        if (!org.springframework.util.StringUtils.hasText(id)) {
            throw new BusinessException("角色ID不能为空");
        }

        RoleDO role = this.getById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        RoleDetailRespDTO respDTO = new RoleDetailRespDTO();
        respDTO.setId(role.getId());
        respDTO.setRoleCode(role.getRoleCode());
        respDTO.setRoleName(role.getRoleName());
        respDTO.setRemark(role.getRemark());
        respDTO.setMenuIds(roleMenuService.listMenuIdsByRoleId(id));
        respDTO.setPermIds(rolePermissionService.listPermIdsByRoleId(id));
        return respDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(String id) {
        if (!org.springframework.util.StringUtils.hasText(id)) {
            throw new BusinessException("角色ID不能为空");
        }

        RoleDO role = this.getById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        ensureNotSuperAdmin(role);

        long bindCount = userRoleService.countByRoleId(id);
        if (bindCount > 0) {
            throw new BusinessException("角色已被用户绑定，不能删除");
        }

        this.removeById(id);
        roleMenuService.replaceMenus(id, Collections.emptyList());
        rolePermissionService.replacePermissions(id, Collections.emptyList());
    }

    private RolePageRespDTO toRolePageResp(RoleDO role) {
        RolePageRespDTO dto = new RolePageRespDTO();
        dto.setId(role.getId());
        dto.setRoleCode(role.getRoleCode());
        dto.setRoleName(role.getRoleName());
        dto.setRemark(role.getRemark());
        return dto;
    }

    private void validateSaveReq(RoleSaveReqDTO dto, boolean update) {
        if (dto == null) {
            throw new BusinessException("请求参数不能为空");
        }
        if (update && !org.springframework.util.StringUtils.hasText(dto.getId())) {
            throw new BusinessException("角色ID不能为空");
        }
        if (StringUtils.isBlank(dto.getRoleCode())) {
            throw new BusinessException("角色编码不能为空");
        }
        if (RoleEnums.SUPER_ADMIN.getCode().equals(dto.getRoleCode().trim())) {
            throw new BusinessException("SUPER_ADMIN为系统保留角色编码");
        }
        if (StringUtils.isBlank(dto.getRoleName())) {
            throw new BusinessException("角色名称不能为空");
        }
    }

    private void checkRoleCodeUnique(String roleCode, String excludeId) {
        LambdaQueryWrapper<RoleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleDO::getRoleCode, roleCode.trim());
        if (org.springframework.util.StringUtils.hasText(excludeId)) {
            wrapper.ne(RoleDO::getId, excludeId);
        }
        long count = this.count(wrapper);
        if (count > 0) {
            throw new BusinessException("角色编码已存在");
        }
    }

    private void ensureNotSuperAdmin(RoleDO role) {
        if (role != null && RoleEnums.SUPER_ADMIN.getCode().equals(role.getRoleCode())) {
            throw new BusinessException("超级管理员角色不允许修改或删除");
        }
    }

    private List<String> normalizeIds(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        Set<String> uniqueIds = ids.stream()
                .filter(org.springframework.util.StringUtils::hasText)
                .map(String::trim)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return new ArrayList<>(uniqueIds);
    }

    private void validateMenuIds(List<String> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return;
        }
        long count = menuService.lambdaQuery().in(MenuDO::getId, menuIds).count();
        if (count != menuIds.size()) {
            throw new BusinessException("菜单ID存在无效值");
        }
    }

    private void validatePermIds(List<String> permIds) {
        if (CollectionUtils.isEmpty(permIds)) {
            return;
        }
        long count = permissionService.lambdaQuery().in(PermissionDO::getId, permIds).count();
        if (count != permIds.size()) {
            throw new BusinessException("权限ID存在无效值");
        }
    }

    private String normalizeNullableText(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value.trim();
    }
}
