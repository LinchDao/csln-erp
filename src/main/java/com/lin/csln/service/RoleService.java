package com.lin.csln.service;

import com.lin.csln.entity.RoleDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色表 服务接口
 * @author 系统生成器
 */
public interface RoleService extends IService<RoleDO> {

    List<RoleDO> listRolesByUserId(Long id);
}
