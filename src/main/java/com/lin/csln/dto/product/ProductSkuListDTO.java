package com.lin.csln.dto.product;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description:
 * @Author: linch
 */
@Data
@Schema(description = "商品SKU列表VO")
public class ProductSkuListDTO {

    @Schema(description = "SKU主键ID")
    private String id;

    @Schema(description = "商品ID")
    private String productId;

    @Schema(description = "颜色名称")
    private String colorName;

    @Schema(description = "尺码名称")
    private String sizeName;
}
