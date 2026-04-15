package com.lin.csln.dto.stock.warehouse;


/**
 * @Description:
 * @Author: linch
 */


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "仓库下拉列表DTO")
public class WarehouseListDTO {

    @Schema(description = "仓库ID")
    private String id;

    @Schema(description = "仓库名称")
    private String warehouseName;
}
