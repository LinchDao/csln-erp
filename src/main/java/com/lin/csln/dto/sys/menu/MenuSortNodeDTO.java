package com.lin.csln.dto.sys.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "菜单排序节点")
public class MenuSortNodeDTO {

    @Schema(description = "菜单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "子节点")
    private List<MenuSortNodeDTO> children;
}
