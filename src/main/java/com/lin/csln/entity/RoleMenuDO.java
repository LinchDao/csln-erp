package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 角色菜单关联表 实体类
 * @author 系统生成器
 */
@Data
@TableName("sys_role_menu")
public class RoleMenuDO {

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
     * 菜单ID
     */
    private String menuId;

    /**
     * 创建时间
     */
    private Date createTime;

}
