package com.lin.csln.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 请求体：商品款号筛选
 */
@Data
@Schema(description = "商品列表下拉筛选参数")
public class ProductSelectQueryDTO {

    @Schema(description = "商品款号，可空")
    private String productNo;
}
