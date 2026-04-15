package com.lin.csln.dto.sys.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "角色分页响应")
public class RolePageRespDTO {

    @Schema(description = "角色ID")
    private String id;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "备注")
    private String remark;
}
