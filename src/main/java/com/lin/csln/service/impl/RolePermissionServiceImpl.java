package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.csln.entity.RolePermissionDO;
import com.lin.csln.mapper.RolePermissionMapper;
import com.lin.csln.service.RolePermissionService;
import com.lin.csln.service.impl.BaseReadonlyServiceImpl;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色权限表 服务实现类
 * @author 系统生成器
 */
@Service
public class RolePermissionServiceImpl extends BaseReadonlyServiceImpl<RolePermissionMapper, RolePermissionDO> implements RolePermissionService {

    @Override
    public List<String> listPermIdsByRoleIds(List<String> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<RolePermissionDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(RolePermissionDO::getRoleId, roleIds);
        wrapper.select(RolePermissionDO::getPermId);
        List<RolePermissionDO> rolePermissionList = this.list(wrapper);
        if (rolePermissionList == null || rolePermissionList.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> permIdSet = rolePermissionList.stream()
                .map(RolePermissionDO::getPermId)
                .filter(StringUtils::hasText)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return new ArrayList<>(permIdSet);
    }
}
