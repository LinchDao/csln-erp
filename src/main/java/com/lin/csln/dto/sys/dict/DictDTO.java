package com.lin.csln.dto.sys.dict;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: linch
 */
@Data
@Schema(description = "数据字典新增请求参数")
public class DictDTO {
    @Schema(description = "id")
    private String id;
    @Schema(description = "parentId")
    private String parentId;
    @Schema(description = "字典名称", required = true)
    @NotBlank(message = "字典名称不能为空")
    private String dictName;

    @Schema(description = "字典值")
    private String dictValue;

    @Schema(description = "排序号（越小越靠前）", required = true)
    @NotNull(message = "排序号不能为空")
    private Integer sort;

    @Schema(description = "状态 1-启用 0-禁用")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "字典列表")
    private List<DictDTO> dictList;

}
