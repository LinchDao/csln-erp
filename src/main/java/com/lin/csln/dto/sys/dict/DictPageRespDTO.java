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
    @Schema(description = "id", example = "0")
    private String id;

    @Schema(description = "字典名称", required = true, example = "男")
    private String dictName;

    @Schema(description = "字典值", example = "1")
    private String dictValue;

    @Schema(description = "状态 1-启用 0-禁用", example = "1")
    private Integer status;

    @Schema(description = "备注", example = "性别枚举")
    private String remark;
}
