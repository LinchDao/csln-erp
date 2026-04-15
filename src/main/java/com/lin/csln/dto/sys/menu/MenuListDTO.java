package com.lin.csln.dto.sys.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "菜单下拉DTO")
public class MenuListDTO {

    @Schema(description = "菜单ID")
    private String id;

    @Schema(description = "父级菜单ID")
    private String parentId;

    @Schema(description = "菜单标题")
    private String title;
}
