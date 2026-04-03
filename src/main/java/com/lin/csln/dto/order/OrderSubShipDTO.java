package com.lin.csln.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "子订单发货参数")
public class OrderSubShipDTO {

    @NotBlank(message = "子订单ID不能为空")
    @Schema(description = "子订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderSubId;

    @Schema(description = "配送方式 0快递 1即时货运 3自提")
    private Integer deliveryType;

    @Schema(description = "快递单号")
    private String expressNo;

    @Schema(description = "即时货运/自提手机号")
    private String driverPhone;

    @Schema(description = "配送备注")
    private String deliveryRemark;

    @Schema(description = "实际发货日期（yyyy-MM-dd）")
    private String actualSendDate;
}
