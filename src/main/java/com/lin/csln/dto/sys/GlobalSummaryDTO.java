package com.lin.csln.dto.sys;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 核心指标统计 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "全局核心指标统计")
public class GlobalSummaryDTO {

    @Schema(description = "客户总数", example = "128")
    private Long customerCount;

    @Schema(description = "订单总金额（不含草稿）", example = "256800.00")
    private BigDecimal totalOrderAmount;

    @Schema(description = "待出库件数", example = "980")
    private Long pendingShipmentQty;

    @Schema(description = "已出库件数", example = "1520")
    private Long shippedQty;
}
