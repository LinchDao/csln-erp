package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.dto.sys.permission.PermissionListDTO;
import com.lin.csln.entity.PermissionDO;
import com.lin.csln.entity.RolePermissionDO;
import com.lin.csln.mapper.PermissionMapper;
import com.lin.csln.service.PermissionService;
import com.lin.csln.service.RolePermissionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl extends BaseReadonlyServiceImpl<PermissionMapper, PermissionDO> implements PermissionService {

    @Resource
    private RolePermissionService rolePermissionService;

    @Override
    public List<String> listPermCodesByPermIds(List<String> permIds) {
        if (permIds == null || permIds.isEmpty()) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<PermissionDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(PermissionDO::getId, permIds);
        wrapper.select(PermissionDO::getPermCode);
        List<PermissionDO> permissionList = this.list(wrapper);
        if (permissionList == null || permissionList.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> permCodeSet = permissionList.stream()
                .map(PermissionDO::getPermCode)
                .filter(StringUtils::hasText)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return new ArrayList<>(permCodeSet);
    }

    @Override
    public List<PermissionListDTO> listPermissionForSelect() {
        LambdaQueryWrapper<PermissionDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(PermissionDO::getPermName);

        List<PermissionDO> permissionList = this.list(wrapper);
        return permissionList.stream().map(this::toPermissionDTO).collect(Collectors.toList());
    }

    @Override
    public PageRespDTO<PermissionListDTO> pagePermission(PermissionListDTO queryDTO) {
        PermissionListDTO query = queryDTO == null ? new PermissionListDTO() : queryDTO;
        int pageNo = query.getPage() == null || query.getPage() < 1 ? 1 : query.getPage();
        int pageSize = query.getLimit() == null || query.getLimit() < 1 ? 10 : query.getLimit();

        Page<PermissionDO> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<PermissionDO> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getPermCode())) {
            wrapper.like(PermissionDO::getPermCode, query.getPermCode().trim());
        }
        if (StringUtils.hasText(query.getPermName())) {
            wrapper.like(PermissionDO::getPermName, query.getPermName().trim());
        }
        wrapper.orderByAsc(PermissionDO::getPermName);

        IPage<PermissionDO> resultPage = this.page(page, wrapper);
        List<PermissionListDTO> rows;
        if (resultPage.getRecords() == null || resultPage.getRecords().isEmpty()) {
            rows = new ArrayList<>();
        } else {
            rows = resultPage.getRecords().stream().map(this::toPermissionDTO).collect(Collectors.toList());
        }
        return PageRespDTO.of(resultPage.getTotal(), rows, pageNo, pageSize);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addPermission(PermissionListDTO permission) {
        if (permission == null) {
            throw new BusinessException("权限参数不能为空");
        }
        String permCode = trimText(permission.getPermCode());
        String permName = trimText(permission.getPermName());
        if (!StringUtils.hasText(permCode)) {
            throw new BusinessException("权限编码不能为空");
        }
        if (!StringUtils.hasText(permName)) {
            throw new BusinessException("权限名称不能为空");
        }

        validatePermCodeUnique(permCode, null);

        PermissionDO saveDO = new PermissionDO();
        saveDO.setPermCode(permCode);
        saveDO.setPermName(permName);
        this.save(saveDO);
        return saveDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(PermissionListDTO permission) {
        if (permission == null || !StringUtils.hasText(permission.getId())) {
            throw new BusinessException("权限ID不能为空");
        }
        PermissionDO dbPermission = this.getById(permission.getId());
        if (dbPermission == null) {
            throw new BusinessException("权限不存在");
        }

        String permCode = trimText(permission.getPermCode());
        String permName = trimText(permission.getPermName());
        if (!StringUtils.hasText(permCode)) {
            throw new BusinessException("权限编码不能为空");
        }
        if (!StringUtils.hasText(permName)) {
            throw new BusinessException("权限名称不能为空");
        }

        validatePermCodeUnique(permCode, permission.getId());

        PermissionDO updateDO = new PermissionDO();
        updateDO.setId(permission.getId());
        updateDO.setPermCode(permCode);
        updateDO.setPermName(permName);
        this.updateById(updateDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(String id) {
        if (!StringUtils.hasText(id)) {
            throw new BusinessException("权限ID不能为空");
        }
        PermissionDO dbPermission = this.getById(id);
        if (dbPermission == null) {
            throw new BusinessException("权限不存在");
        }

        LambdaQueryWrapper<RolePermissionDO> bindWrapper = new LambdaQueryWrapper<>();
        bindWrapper.eq(RolePermissionDO::getPermId, id);
        long bindCount = rolePermissionService.count(bindWrapper);
        if (bindCount > 0) {
            throw new BusinessException("权限已被角色绑定，无法删除");
        }

        this.removeById(id);
    }

    @Override
    public PermissionListDTO getPermissionDetail(String id) {
        if (!StringUtils.hasText(id)) {
            throw new BusinessException("权限ID不能为空");
        }
        PermissionDO permission = this.getById(id);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }
        return toPermissionDTO(permission);
    }

    private void validatePermCodeUnique(String permCode, String excludeId) {
        LambdaQueryWrapper<PermissionDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PermissionDO::getPermCode, permCode);
        if (StringUtils.hasText(excludeId)) {
            wrapper.ne(PermissionDO::getId, excludeId);
        }
        long count = this.count(wrapper);
        if (count > 0) {
            throw new BusinessException("权限编码已存在");
        }
    }

    private String trimText(String value) {
        return value == null ? null : value.trim();
    }

    private PermissionListDTO toPermissionDTO(PermissionDO permissionDO) {
        PermissionListDTO dto = new PermissionListDTO();
        if (permissionDO == null) {
            return dto;
        }
        dto.setId(permissionDO.getId());
        dto.setPermCode(permissionDO.getPermCode());
        dto.setPermName(permissionDO.getPermName());
        return dto;
    }
}
