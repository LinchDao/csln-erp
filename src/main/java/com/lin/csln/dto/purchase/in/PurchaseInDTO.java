package com.lin.csln.dto.purchase.in;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: linch
 */

@Data
@Schema(description = "采购入库单创建DTO")
public class PurchaseInDTO {

    @NotBlank(message = "采购单ID不能为空")
    @Schema(description = "采购单ID")
    private String purchaseId;

    @NotBlank(message = "入库仓库ID不能为空")
    @Schema(description = "入库仓库ID")
    private String warehouseId;

    @Schema(description = "备注")
    private String remark;

    @NotNull(message = "入库明细不能为空")
    @Schema(description = "入库明细列表")
    private List<PurchaseInItemDTO> itemList;
}