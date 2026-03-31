package com.lin.csln.dto.purchase.in;


import com.lin.csln.common.dto.PageQueryParamDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description:
 * @Author: linch
 */

@Data
@Schema(description = "采购入库单分页查询DTO")
public class PurchaseInQueryParamDTO extends PageQueryParamDTO {
    @Schema(description = "入库单号")
    private String inNo;

    @Schema(description = "来源采购单号")
    private String purchaseNo;

    @Schema(description = "仓库名称")
    private String warehouseId;

    @Schema(description = "入库状态 0待审核 1已审核")
    private Integer status;
}

