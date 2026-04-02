package com.lin.csln.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Schema(description = "子订单")
public class OrderSubDTO {

    @Schema(description = "子订单ID")
    private String id;

    @Schema(description = "子订单号")
    private String subOrderNo;

    @Schema(description = "发货仓库ID")
    private String warehouseId;

    @Schema(description = "子单金额")
    private BigDecimal amount;

    @Schema(description = "发货类型")
    private Integer sendType;

    @Schema(description = "来源仓库ID")
    private String fromWarehouseId;

    @Schema(description = "子单状态")
    private Integer status;

    @Schema(description = "配送方式")
    private Integer deliveryType;

    @Schema(description = "快递单号")
    private String expressNo;

    @Schema(description = "司机/自提手机号")
    private String driverPhone;

    @Schema(description = "配送备注")
    private String deliveryRemark;

    @Schema(description = "期望发货日期")
    private Date expectSendDate;

    @Schema(description = "实际发货日期")
    private Date actualSendDate;

    @Schema(description = "配货员ID")
    private String pickerUserId;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "子单明细")
    private List<OrderItemDTO> items;
}
