package com.lin.csln.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "订单分页结果")
public class OrderMasterPageRespDTO {

    @Schema(description = "主单ID")
    private String id;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "门店ID")
    private String shopId;

    @Schema(description = "门店名称")
    private String shopName;

    @Schema(description = "客户ID")
    private String customerId;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "总数量")
    private Integer totalQty;

    @Schema(description = "总金额")
    private BigDecimal totalAmount;

    @Schema(description = "订单类型")
    private Integer orderType;

    @Schema(description = "订单状态")
    private Integer status;

    @Schema(description = "是否草稿 0正式单 1草稿单")
    private Integer isDraft;

    @Schema(description = "开单时间")
    private String createTime;

    @Schema(description = "发货仓库名称(逗号分隔)")
    private String warehouseNames;
}
