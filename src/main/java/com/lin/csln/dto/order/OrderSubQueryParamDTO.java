package com.lin.csln.dto.order;


import com.lin.csln.common.dto.PageQueryParamDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "子订单分页查询参数")
@Data
public class OrderSubQueryParamDTO extends PageQueryParamDTO {

    @Schema(description = "子订单号")
    private String subOrderNo;

    @Schema(description = "客户ID")
    private String customerId;

    @Schema(description = "开单人ID")
    private String createUserId;

    @Schema(description = "发货日期起")
    private String sendDateStart;

    @Schema(description = "发货日期止")
    private String sendDateEnd;

    @Schema(description = "子单状态")
    private Integer subOrderStatus;
}
