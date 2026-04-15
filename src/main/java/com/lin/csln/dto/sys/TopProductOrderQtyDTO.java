package com.lin.csln.dto.sys;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 近一个月订单量 TOP 商品 DTO
 */
@Data
@Schema(description = "近一个月订单量前五商品")
public class TopProductOrderQtyDTO {

    @Schema(description = "款号")
    private String productNo;

    @Schema(description = "名称")
    private String productName;

    @Schema(description = "件数")
    private Long qty;
}
