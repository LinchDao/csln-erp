package com.lin.csln.dto.product;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品SKU DTO
 *
 * @Description:
 * @Author: linch
 */


@Data
@Schema(name = "ProductSkuDTO", description = "商品SKU DTO")
public class ProductSkuDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "商品ID", example = "1")
    private Long productId;

    @Schema(description = "颜色ID", example = "2")
    private Long colorId;

    @Schema(description = "颜色名称（冗余字段）", example = "黑色")
    private String colorName;

    @Schema(description = "尺码ID", example = "3")
    private Long sizeId;

    @Schema(description = "尺码名称（冗余字段）", example = "XL")
    private String sizeName;

    @Schema(description = "条码", example = "6971234567890")
    private String barcode;
}
