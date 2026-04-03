package com.lin.csln.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description: 子订单分页结果
 * @Author: linch
 */
@Schema(description = "子订单分页结果")
@Data
public class OrderSubPageRespDTO {

    @Schema(description = "子订单ID")
    private String id;

    @Schema(description = "主订单号")
    private String orderNo;

    @Schema(description = "子订单号")
    private String subOrderNo;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "开单人")
    private String createUserName;

    @JsonIgnore
    private String createUserId;

    @Schema(description = "订单类型")
    private Integer orderType;

    @Schema(description = "订单状态")
    private Integer status;

    @Schema(description = "订单备注")
    private String remark;

    @Schema(description = "子单状态")
    private Integer subOrderStatus;

    @Schema(description = "配送方式 0快递 1即时货运 3自提")
    private Integer deliveryType;

    @Schema(description = "配送备注")
    private String deliveryRemark;

    @Schema(description = "期望发货日期")
    private String expectSendDate;

    @Schema(description = "实际发货日期")
    private String actualSendDate;

    @Schema(description = "配货员")
    private String pickerUserName;

    @JsonIgnore
    private String pickerUserId;

    @Schema(description = "子单备注")
    private String subRemark;
}
