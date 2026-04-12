package com.lin.csln.dto.sys.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "角色新增/修改请求")
public class RoleSaveReqDTO {

    @Schema(description = "角色ID，修改时必填")
    private String id;

    @Schema(description = "角色编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleCode;

    @Schema(description = "角色名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleName;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "菜单ID列表")
    private List<String> menuIds;

    @Schema(description = "权限ID列表")
    private List<String> permIds;
}
