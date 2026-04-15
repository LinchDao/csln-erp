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

    @Schema(description = "主键ID")
    private String id;
    @Schema(description = "商品ID")
    private String productId;
    @Schema(description = "颜色名称（冗余字段）")
    private String colorName;
    @Schema(description = "尺码名称（冗余字段）")
    private String sizeName;
    @Schema(description = "条码")
    private String barcode;
    @Schema(description = "'可用库存'")
    private Integer qty;
    @Schema(description = "''锁定库存''")
    private Integer lockQty;
    @Schema(description = "'追回货预占'")
    private Integer recoveryQty;
    @Schema(description = "'预警数量'")
    private Integer warnQty;
}


