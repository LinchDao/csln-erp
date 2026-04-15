package com.lin.csln.dto.purchase.in;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description:
 * @Author: linch
 */
@Data
@Schema(description = "采购入库单分页返回PurchaseInPageRespDTO")
public class PurchaseInPageRespDTO {

    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "入库单号")
    private String inNo;

    @Schema(description = "来源采购单ID")
    private String purchaseId;

    @Schema(description = "来源采购单号")
    private String purchaseNo;

    @Schema(description = "仓库ID")
    private String warehouseId;

    @Schema(description = "仓库名称")
    private String warehouseName;

    @Schema(description = "入库总数量")
    private Integer totalQty;

    @Schema(description = "入库状态 0待审核 1已审核")
    private Integer status;

    @Schema(description = "操作人ID")
    private String createUserId;

    @Schema(description = "审核人ID")
    private String auditUserId;

    @Schema(description = "操作人名称")
    private String createUserName;

    @Schema(description = "审核人名称")
    private String auditUserName;

    @Schema(description = "入库时间")
    private String createTime;

    @Schema(description = "备注")
    private String remark;
}

