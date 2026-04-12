package com.lin.csln.dto.sys.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "权限下拉DTO")
public class PermissionListDTO {

    @Schema(description = "权限ID")
    private String id;

    @Schema(description = "权限名称")
    private String permName;
}
