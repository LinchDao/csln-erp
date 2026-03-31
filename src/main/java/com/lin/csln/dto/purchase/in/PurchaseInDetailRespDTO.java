package com.lin.csln.dto.purchase.in;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: linch
 */

@Data
@Schema(description = "采购入库单详情返回DTO")
public class PurchaseInDetailRespDTO {

    @Schema(description = "入库单主键ID")
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

    @Schema(description = "入库人ID")
    private String createUserId;

    @Schema(description = "入库人名称")
    private String createUserName;

    @Schema(description = "审核人ID")
    private String auditUserId;

    @Schema(description = "审核人名称")
    private String auditUserName;

    @Schema(description = "入库时间（格式化：yyyy-MM-dd HH:mm:ss）")
    private String createTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "入库商品明细列表")
    private List<PurchaseInItemRespDTO> purchaseInItem;
}
