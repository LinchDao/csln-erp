package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.csln.entity.RoleDO;
import com.lin.csln.mapper.RoleMapper;
import com.lin.csln.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 角色表 服务实现类
 *
 * @author 系统生成器
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleDO> implements RoleService {

    @Override
    public List<RoleDO> listRolesByUserId(Long userId) {
        if (userId == null || userId <= 0) {
            return Collections.emptyList();
        }

        try {
            List<RoleDO> roles = baseMapper.listRolesByUserId(userId);
            return roles != null ? roles : Collections.emptyList();

        } catch (Exception e) {

            return Collections.emptyList();
        }
    }
}
