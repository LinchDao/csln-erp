package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.csln.common.dto.UserInfoDTO;
import com.lin.csln.entity.RoleDO;
import com.lin.csln.entity.UserDO;
import com.lin.csln.mapper.UserMapper;
import com.lin.csln.service.RoleService;
import com.lin.csln.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表 服务实现类
 *
 * @author 系统生成器
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    @Resource
    private RoleService roleService;

    @Override
    public UserDO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getUsername, username);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public UserInfoDTO getUserInfoByUsername(String username) {

        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getUsername, username);
        UserDO user = baseMapper.selectOne(queryWrapper);
        if (user == null) {
            return null;
        }

        return getUserInfoById(user.getId());
    }

    /**
     * 根据用户ID获取用户信息DTO（包含角色编码列表）
     *
     * @param id 用户ID
     * @return 用户信息DTO
     */
    @Override
    public UserInfoDTO getUserInfoById(String id) {
        if (id == null) {
            return null;
        }
        UserDO userDO = baseMapper.selectById(id);

        if (userDO == null) {
            return null;
        }

        // 2. 查询用户角色信息
        List<RoleDO> roles = roleService.listRolesByUserId(id);

        // 3. 转换为DTO
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(userDO, userInfoDTO);

        // 4. 提取角色编码列表
        if (!CollectionUtils.isEmpty(roles)) {
            List<String> roleCodes = roles.stream()
                    .map(RoleDO::getRoleCode)
                    .collect(Collectors.toList());
            userInfoDTO.setRoles(roleCodes);
        }

        return userInfoDTO;
    }

}
