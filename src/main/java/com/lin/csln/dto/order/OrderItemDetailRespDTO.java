package com.lin.csln.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "订单明细详情")
public class OrderItemDetailRespDTO {

    @Schema(description = "商品明细 ID")
    private String id;

    @Schema(description = "关联子订单 ID")
    private String subId;

    @Schema(description = "商品 ID")
    private String productId;

    @Schema(description = "SKU 主键 ID")
    private String skuId;

    @Schema(description = "SKU规格快照JSON")
    private String skuSpecSnapshot;

    @Schema(description = "款号")
    private String productNo;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品数量")
    private Integer qty;

    @Schema(description = "商品单价")
    private BigDecimal price;

    @Schema(description = "单项金额")
    private BigDecimal amount;
}
