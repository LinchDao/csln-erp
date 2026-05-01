package com.lin.csln.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "订单明细")
public class OrderItemDTO {

    @Schema(description = "明细ID")
    private String id;

    @Schema(description = "子单ID")
    private String subId;

    @Schema(description = "SKU ID")
    private String skuId;

    @Schema(description = "SKU规格快照JSON")
    private String skuSpecSnapshot;

    @Schema(description = "下单数量")
    private Integer qty;

    @Schema(description = "单价")
    private BigDecimal price;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "实际发货数量")
    private Integer actualQty;
}
