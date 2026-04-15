package com.lin.csln.dto.purchase.in;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Description:
 * @Author: linch
 */

@Data
@Schema(description = "采购入库单明细DTO")
public class PurchaseInItemDTO {

    @NotBlank(message = "SKU_ID不能为空")
    @Schema(description = "SKU_ID")
    private String skuId;

    @NotNull(message = "入库数量不能为空")
    @Schema(description = "入库数量")
    private Integer qty;
}