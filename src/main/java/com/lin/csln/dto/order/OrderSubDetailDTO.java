package com.lin.csln.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "子订单详情")
public class OrderSubDetailDTO {

    @Schema(description = "子订单ID")
    private String id;

    @Schema(description = "主订单号")
    private String orderNo;

    @Schema(description = "子订单号")
    private String subOrderNo;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "仓库名称")
    private String warehouseName;

    @Schema(description = "配送方式 0快递 1即时货运 3自提")
    private Integer deliveryType;

    @Schema(description = "子单状态")
    private Integer status;

    @Schema(description = "期望发货日期")
    private String expectSendDate;

    @Schema(description = "实际发货日期")
    private String actualSendDate;

    @Schema(description = "配货员名称")
    private String pickerUserName;

    @Schema(description = "快递单号")
    private String expressNo;

    @Schema(description = "配送备注")
    private String deliveryRemark;

    @Schema(description = "子单备注")
    private String remark;

    @Schema(description = "子单金额")
    private BigDecimal amount;

    @JsonIgnore
    @Schema(description = "配货员ID")
    private String pickerUserId;

    @Schema(description = "子单明细")
    private List<OrderItemDetailRespDTO> items;
}
