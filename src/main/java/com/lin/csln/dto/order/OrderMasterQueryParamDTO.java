package com.lin.csln.dto.order;

import com.lin.csln.common.dto.PageQueryParamDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "订单分页查询参数")
public class OrderMasterQueryParamDTO extends PageQueryParamDTO {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "订单类型")
    private Integer orderType;

    @Schema(description = "订单状态")
    private Integer status;

    @Schema(description = "是否草稿 0正式单 1草稿单")
    private Integer isDraft;
}
