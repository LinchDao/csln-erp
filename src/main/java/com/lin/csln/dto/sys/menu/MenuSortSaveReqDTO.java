package com.lin.csln.dto.sys.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "菜单排序保存请求")
public class MenuSortSaveReqDTO {

    @Schema(description = "菜单树（全量）", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<MenuSortNodeDTO> menus;
}
