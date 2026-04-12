package com.lin.csln.dto.sys.role;

import com.lin.csln.common.dto.PageQueryParamDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "角色分页查询参数")
public class RoleQueryParamDTO extends PageQueryParamDTO {

    @Schema(description = "角色编码（模糊）")
    private String roleCode;

    @Schema(description = "角色名称（模糊）")
    private String roleName;
}
