package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.csln.dto.sys.role.RoleListDTO;
import com.lin.csln.entity.RoleDO;
import com.lin.csln.enums.RoleEnums;
import com.lin.csln.mapper.RoleMapper;
import com.lin.csln.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色表 服务实现类
 *
 * @author 系统生成器
 */
@Service
public class RoleServiceImpl extends BaseReadonlyServiceImpl<RoleMapper, RoleDO> implements RoleService {

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
}
