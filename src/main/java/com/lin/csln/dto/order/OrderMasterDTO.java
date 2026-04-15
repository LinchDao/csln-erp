package com.lin.csln.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Schema(description = "订单主单")
public class OrderMasterDTO {

    @Schema(description = "主单ID")
    private String id;

    @Schema(description = "客户ID")
    private String customerId;

    @Schema(description = "总数量")
    private Integer totalQty;

    @Schema(description = "总金额")
    private BigDecimal totalAmount;

    @Schema(description = "订单类型")
    private Integer orderType;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "是否草稿 0正式单 1草稿单")
    private Integer isDraft;

    @Schema(description = "子订单列表")
    private List<OrderSubDTO> subOrders;
}
