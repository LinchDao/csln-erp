package com.lin.csln.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.common.dto.UserInfoDTO;
import com.lin.csln.entity.UserDO;

/**
 * 用户表 服务接口
 *
 * @author 系统生成器
 */
public interface UserService extends IService<UserDO> {

    UserDO getUserByUsername(String username);

    UserInfoDTO getUserInfoByUsername(String username);

    UserInfoDTO getUserInfoById(Long userId);
}
