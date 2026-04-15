package com.lin.csln.dto.product;


/**
 * @Description:
 * @Author: linch
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "商品下拉选择DTO")
public class ProductSelectDTO {

    @Schema(description = "商品ID")
    private String id;
    @Schema(description = "商品款号")
    private String productNo;
    @Schema(description = "商品名称")
    private String name;
    @Schema(description = "默认批发价")
    private BigDecimal wholesalePrice;
    @Schema(description = "零售价")
    private BigDecimal retailPrice;
    @Schema(description = "主图ID")
    private String mainImageId;
}
