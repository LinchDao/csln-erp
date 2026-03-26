package com.lin.csln.common.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description:
 * @Author: linch
 */
@Data
@Schema(name = "PageQueryParamDTO", description = "分页查询DTO")
public class PageQueryParamDTO {
    @Schema(description = "页码（默认1）")
    private Integer page = 1;

    @Schema(description = "页码（默认10）")
    private Integer limit = 10;
}
