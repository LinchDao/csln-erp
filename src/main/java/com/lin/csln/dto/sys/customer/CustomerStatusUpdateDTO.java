package com.lin.csln.dto.sys.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 客户状态更新请求
 */
@Data
@Schema(name = "CustomerStatusUpdateDTO", description = "客户状态更新请求")
public class CustomerStatusUpdateDTO {

    @Schema(description = "客户ID")
    private String customerId;

    @Schema(description = "状态：1-启用，0-禁用")
    private Integer status;
}
