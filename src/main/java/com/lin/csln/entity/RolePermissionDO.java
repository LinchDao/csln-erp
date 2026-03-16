package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 角色权限表 实体类
 * @author 系统生成器
 */
@Data
@TableName("sys_role_permission")
public class RolePermissionDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 权限ID
     */
    private String permId;

}
