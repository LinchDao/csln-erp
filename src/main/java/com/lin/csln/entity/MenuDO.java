package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 菜单表 实体类
 * @author 系统生成器
 */
@Data
@TableName("sys_menu")
public class MenuDO {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父菜单ID，顶级菜单为0
     */
    private Long parentId;

    /**
     * 菜单类型 1目录 2菜单 3按钮
     */
    private Integer menuType;

    /**
     * 路由名称
     */
    private String name;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 重定向路径
     */
    private String redirect;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 是否缓存 0缓存 1不缓存
     */
    private Integer noCache;

    /**
     * 是否显示面包屑 0不显示 1显示
     */
    private Integer breadcrumb;

    /**
     * 是否固定 0不固定 1固定
     */
    private Integer affix;

    /**
     * 是否隐藏 0显示 1隐藏
     */
    private Integer hidden;

    /**
     * 是否总是显示 0否 1是
     */
    private Integer alwaysShow;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 状态 1正常 0禁用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
