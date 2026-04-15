package com.lin.csln.dto.purchase.in;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description: 采购入库单商品明细返回DTO
 * @Author: linch
 */
@Data
@Schema(description = "采购入库单商品明细返回DTO")
public class PurchaseInItemRespDTO {

    @Schema(description = "SKU主键ID")
    private String skuId;

    @Schema(description = "款号")
    private String productNo;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "颜色名称")
    private String colorName;

    @Schema(description = "尺码名称")
    private String sizeName;

    @Schema(description = "采购数量")
    private Integer purchaseQty;

    @Schema(description = "本次入库数量")
    private Integer qty;
}
