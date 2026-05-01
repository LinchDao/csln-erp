package com.lin.csln.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品V2下拉返回DTO
 */
@Data
@Schema(description = "商品V2下拉返回DTO")
public class ProductV2SelectRespDTO {

    @Schema(description = "商品ID")
    private String id;

    @Schema(description = "商品款号")
    private String productNo;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "成本价")
    private BigDecimal costPrice;

    @Schema(description = "批发价")
    private BigDecimal wholesalePrice;

    @Schema(description = "零售价")
    private BigDecimal retailPrice;

    @Schema(description = "主图ID")
    private String mainImageId;
}
