package com.lin.csln.dto.sys.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "角色详情响应")
public class RoleDetailRespDTO {

    @Schema(description = "角色ID")
    private String id;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "菜单ID列表")
    private List<String> menuIds;

    @Schema(description = "权限ID列表")
    private List<String> permIds;
}
