package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 权限表 实体类
 * @author 系统生成器
 */
@Data
@TableName("sys_permission")
public class PermissionDO {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 权限编码
     */
    private String permCode;

    /**
     * 权限名称
     */
    private String permName;

}
