package com.lin.csln.dto.sys.dict;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description:
 * @Author: linch
 */
@Data
@Schema(name = "DictPageRespDTO", description = "字典分页DTO")
public class DictPageRespDTO {
    @Schema(description = "id")
    private String id;

    @Schema(description = "字典名称", required = true)
    private String dictName;

    @Schema(description = "字典值")
    private String dictValue;

    @Schema(description = "状态 1-启用 0-禁用")
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}
