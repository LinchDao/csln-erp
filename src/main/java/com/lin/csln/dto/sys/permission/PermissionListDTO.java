package com.lin.csln.dto.sys.permission;

import com.lin.csln.common.dto.PageQueryParamDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "权限DTO")
public class PermissionListDTO extends PageQueryParamDTO {

    @Schema(description = "权限ID")
    private String id;

    @Schema(description = "权限编码（查询条件，模糊）")
    private String permCode;

    @Schema(description = "权限名称（查询条件，模糊）")
    private String permName;
}
