package com.lin.csln.dto.sys.dict;


import com.lin.csln.common.dto.PageQueryParamDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Description:
 * @Author: linch
 */
@Data
@Schema(name = "DictQueryParamDTO", description = "字典分页查询DTO")
public class DictQueryParamDTO extends PageQueryParamDTO {
    @Schema(description = "字典名称", required = true, example = "男")
    @NotBlank(message = "字典名称不能为空")
    private String dictName;

    @Schema(description = "字典值", example = "1")
    private String dictValue;

    @Schema(description = "状态 1-启用 0-禁用", example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

}
