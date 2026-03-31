package com.lin.csln.dto.purchase.order;


/**
 * @Description:
 * @Author: linch
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "采购单DTO")
public class PurchaseOrderDTO {

    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "采购单号")
    private String purchaseNo;

    @Schema(description = "供应商ID")
    private String supplierId;

    @Schema(description = "供应商名称")
    private String supplierName;

    @Schema(description = "总数量")
    private Integer totalQty;

    @Schema(description = "总金额")
    private BigDecimal totalAmount;

    @Schema(description = "状态 0待入库 1部分入库 2已完成 3取消")
    private Integer status;

    @Schema(description = "制单人ID")
    private String createUserId;

    @Schema(description = "制单人名称")
    private String createUserName;

    @Schema(description = "系统创建时间")
    private String createTime;

    @Schema(description = "下单时间")
    private String orderTime;

    @Schema(description = "预计到货时间")
    private String arrivalTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "采购单明细列表")
    private List<PurchaseOrderItemDTO> purchaseOrderItem;
}
