package com.lin.csln.dto.purchase;


/**
 * @Description:
 * @Author: linch
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "采购单明细DTO")
public class PurchaseOrderItemDTO {
    @Schema(description = "主键")
    private String id;
    @Schema(description = "SKU主键ID")
    private String skuId;
    @Schema(description = "商品ID")
    private String productId;

    @Schema(description = "商品款号")
    private String productNo;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "颜色名称")
    private String colorName;

    @Schema(description = "尺码名称")
    private String sizeName;

    @Schema(description = "采购单价")
    private BigDecimal price;

    @Schema(description = "采购数量")
    private Integer qty;

    @Schema(description = "采购金额（单价×数量）")
    private BigDecimal amount;
}