package com.lin.csln.dto.sys.customer;

import com.lin.csln.common.dto.PageQueryParamDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 客户分页查询参数
 */
@Data
@Schema(name = "CustomerQueryParamDTO", description = "客户分页查询参数")
public class CustomerQueryParamDTO extends PageQueryParamDTO {

    @Schema(description = "客户名称（模糊查询）")
    private String name;

    @Schema(description = "手机号（模糊查询）")
    private String phone;

    @Schema(description = "状态：1-启用，0-禁用")
    private Integer status;
}
