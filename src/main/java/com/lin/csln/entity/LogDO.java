package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 系统操作日志 实体类
 * @author 系统生成器
 */
@Data
@TableName("sys_log")
public class LogDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 操作人
     */
    private String userId;

    /**
     * 模块
     */
    private String module;

    /**
     * 操作内容
     */
    private String content;

    /**
     * 修改前
     */
    private String beforeData;

    /**
     * 修改后
     */
    private String afterData;

    /**
     * 操作时间
     */
    private Date createTime;

}
