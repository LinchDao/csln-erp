package com.lin.csln.dto.sys.log;

import com.lin.csln.common.dto.PageQueryParamDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 系统操作日志分页查询参数
 */
@Data
@Schema(name = "LogQueryParamDTO", description = "系统操作日志分页查询参数")
public class LogQueryParamDTO extends PageQueryParamDTO {

    @Schema(description = "业务主键")
    private String bizId;

    @Schema(description = "所属模块")
    private String module;

    @Schema(description = "动作类型")
    private String actionType;

    @Schema(description = "操作人ID")
    private String userId;

    @Schema(description = "执行状态 0-失败 1-成功")
    private Integer status;

    @Schema(description = "操作时间起")
    private Date startTime;

    @Schema(description = "操作时间止")
    private Date endTime;
}
