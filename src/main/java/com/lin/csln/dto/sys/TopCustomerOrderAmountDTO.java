package com.lin.csln.dto.sys;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 近一个月客户订单金额 TOP DTO
 */
@Data
@Schema(description = "近一个月客户订单金额排名")
public class TopCustomerOrderAmountDTO {

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "订单金额")
    private BigDecimal orderAmount;

    @Schema(description = "该客户订单商品件数前三")
    private List<TopCustomerProductQtyDTO> topProducts;
}
