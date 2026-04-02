package com.lin.csln.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "子订单详情")
public class OrderSubDetailRespDTO {

    @Schema(description = "子订单 ID")
    private String id;

    @Schema(description = "仓库 ID")
    private String warehouseId;

    @Schema(description = "子订单金额")
    private BigDecimal amount;

    @Schema(description = "配送方式编码")
    private Integer deliveryType;

    @Schema(description = "配送备注")
    private String deliveryRemark;

    @Schema(description = "期望发货日期（yyyy-MM-dd）")
    private String expectSendDate;

    @Schema(description = "子订单明细")
    private List<OrderItemDetailRespDTO> items;
}
