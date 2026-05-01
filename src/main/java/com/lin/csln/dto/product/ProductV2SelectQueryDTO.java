package com.lin.csln.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品V2下拉查询参数
 */
@Data
@Schema(description = "商品V2下拉查询参数")
public class ProductV2SelectQueryDTO {

    @Schema(description = "关键词，支持匹配款号和名称")
    private String keyword;
}
