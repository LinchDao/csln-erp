package com.lin.csln.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lin.csln.entity.UserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.csln.dto.sys.user.UserPageRespDTO;
import com.lin.csln.dto.sys.user.UserQueryParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户表 Mapper接口
 * @author 系统生成器
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

    IPage<UserPageRespDTO> pageUser(IPage<UserPageRespDTO> page, @Param("params") UserQueryParamDTO queryDTO);
}
