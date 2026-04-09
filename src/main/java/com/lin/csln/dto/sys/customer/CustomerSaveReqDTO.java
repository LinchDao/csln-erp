package com.lin.csln.dto.sys.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 客户新增/修改请求
 */
@Data
@Schema(name = "CustomerSaveReqDTO", description = "客户新增/修改请求")
public class CustomerSaveReqDTO {

    @Schema(description = "客户ID，更新时必填")
    private String id;

    @Schema(description = "客户名称")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "等级ID，可空")
    private String levelId;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "状态：1-启用，0-禁用")
    private Integer status;
}
