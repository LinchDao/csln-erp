package com.lin.csln.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.dto.UserInfoDTO;
import com.lin.csln.dto.sys.user.UserDetailRespDTO;
import com.lin.csln.dto.sys.user.UserPageRespDTO;
import com.lin.csln.dto.sys.user.UserPasswordUpdateDTO;
import com.lin.csln.dto.sys.user.UserQueryParamDTO;
import com.lin.csln.dto.sys.user.UserSaveReqDTO;
import com.lin.csln.dto.sys.user.UserStatusUpdateDTO;
import com.lin.csln.entity.UserDO;

import java.util.Map;
import java.util.Set;

/**
 * 用户表 服务接口
 *
 * @author 系统生成器
 */
public interface UserService extends IService<UserDO> {

    UserDO getUserByUsername(String username);

    UserInfoDTO getUserInfoByUsername(String username);

    UserInfoDTO getUserInfoById(String userId);

    Map<String, String> getUserNamesByIds(Set<String> userIds);

    PageRespDTO<UserPageRespDTO> pageUser(UserQueryParamDTO queryDTO);

    String addUser(UserSaveReqDTO dto);

    void updateUser(UserSaveReqDTO dto);

    UserDetailRespDTO getUserDetail(String userId);

    void changePassword(UserPasswordUpdateDTO dto);

    void updateUserStatus(UserStatusUpdateDTO dto);
}
