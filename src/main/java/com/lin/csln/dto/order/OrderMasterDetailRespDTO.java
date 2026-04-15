package com.lin.csln.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "订单主单详情")
public class OrderMasterDetailRespDTO {

    @Schema(description = "主订单 ID")
    private String id;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "客户 ID")
    private String customerId;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "客户电话")
    private String customerPhone;

    @Schema(description = "订单类型编码")
    private Integer orderType;

    @Schema(description = "订单状态编码")
    private Integer status;

    @Schema(description = "商品总数量")
    private Integer totalQty;

    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    @Schema(description = "是否草稿（1=草稿，0=正式）")
    private Integer isDraft;

    @Schema(description = "AR 标识")
    private Integer isAr;

    @Schema(description = "允许替换标识")
    private Integer allowReplace;

    @Schema(description = "订单备注")
    private String remark;

    @Schema(description = "开单时间（yyyy-MM-dd HH:mm:ss）")
    private String createTime;

    @Schema(description = "子订单列表")
    private List<OrderSubDetailRespDTO> subOrders;
}
