package com.lin.csln.dto.purchase.in;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description:
 * @Author: linch
 */

@Schema(description = "采购单已入库数量")
@Data
public class PurchaseInstockedQtyDTO {

    @Schema(description = "SKU_ID")
    private String skuId;

    @Schema(description = "已入库数量")
    private Integer qty;
}
