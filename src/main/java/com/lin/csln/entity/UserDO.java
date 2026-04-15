package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 用户表 实体类
 * @author 系统生成器
 */
@Data
@TableName("sys_user")
public class UserDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 登录账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 电话
     */
    private String phone;

    /**
     * 所属门店ID
     */
    private String shopId;

    /**
     * 所属仓库ID
     */
    private String warehouseId;

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
