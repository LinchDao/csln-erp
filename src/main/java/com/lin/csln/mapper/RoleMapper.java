package com.lin.csln.mapper;

import com.lin.csln.entity.RoleDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色表 Mapper接口
 * @author 系统生成器
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleDO> {

    /**
     * 根据用户ID查询角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    @Select("SELECT r.* FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<RoleDO> selectRolesByUserId(@Param("userId") String userId);

    /**
     * 根据用户ID查询角色列表（带缓存配置）
     * @param userId 用户ID
     * @return 角色列表
     */
    @Select("SELECT r.id, r.role_code, r.role_name, r.remark " +
            "FROM sys_role r " +
            "LEFT JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<RoleDO> listRolesByUserId(@Param("userId") String userId);

}
