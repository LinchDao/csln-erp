package com.lin.csln.dto.sys;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 销售趋势统计 DTO
 */
@Data
@Schema(description = "销售趋势统计")
public class DailySalesDTO {

    @Schema(description = "统计日期，格式MM=dd", example = "04=08")
    private String day;

    @Schema(description = "当日订单金额", example = "12880.50")
    private BigDecimal salesAmount;

    @Schema(description = "当日出库件数", example = "320")
    private Long shipmentQty;

    @Schema(description = "当日订单件数", example = "356")
    private Long salesQty;
}
