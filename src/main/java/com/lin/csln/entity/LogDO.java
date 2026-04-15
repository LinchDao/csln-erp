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
     * 业务主键
     */
    private String bizId;

    /**
     * 所属模块
     */
    private String module;

    /**
     * 动作类型
     */
    private String actionType;

    /**
     * 差异数据(JSON)
     */
    private String diffData;

    /**
     * 操作人ID
     */
    private String userId;

    /**
     * 请求URL
     */
    private String requestUrl;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 执行状态 0-失败 1-成功
     */
    private Integer status;

    /**
     * 异常信息
     */
    private String errorMsg;

    /**
     * 执行耗时(毫秒)
     */
    private Integer costTime;

    /**
     * 操作时间
     */
    private Date createTime;

}
