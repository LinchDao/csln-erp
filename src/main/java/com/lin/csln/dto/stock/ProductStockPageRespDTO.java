package com.lin.csln.dto.stock;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品库存分页响应
 */
@Data
@Schema(name = "ProductStockPageRespDTO", description = "商品库存分页响应")
public class ProductStockPageRespDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "商品ID")
    private String productId;

    @Schema(description = "款号")
    private String productNo;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "SKU ID")
    private String skuId;

    @Schema(description = "颜色")
    private String colorName;

    @Schema(description = "尺码")
    private String sizeName;

    @Schema(description = "仓库ID")
    private String warehouseId;

    @Schema(description = "仓库名称")
    private String warehouseName;

    @Schema(description = "可用库存")
    private Integer qty;

    @Schema(description = "锁定库存")
    private Integer lockQty;

    @Schema(description = "追回货预占")
    private Integer recoveryQty;

    @Schema(description = "预警数量")
    private Integer warnQty;

    @Schema(description = "更新时间")
    private Date updateTime;
}
