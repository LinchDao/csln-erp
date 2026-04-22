package com.lin.csln.dto.sys.log;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统操作日志分页响应
 */
@Data
@Schema(name = "LogPageRespDTO", description = "系统操作日志分页响应")
public class LogPageRespDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "业务主键")
    private String bizId;

    @Schema(description = "所属模块")
    private String module;

    @Schema(description = "动作类型")
    private String actionType;

    @Schema(description = "操作人ID")
    private String userId;

    @Schema(description = "操作人名称")
    private String userName;

    @Schema(description = "请求URL")
    private String requestUrl;

    @Schema(description = "请求方式")
    private String requestMethod;

    @Schema(description = "执行状态 0-失败 1-成功")
    private Integer status;

    @Schema(description = "异常信息")
    private String errorMsg;

    @Schema(description = "执行耗时(毫秒)")
    private Integer costTime;

    @Schema(description = "操作时间")
    private String createTime;
}
