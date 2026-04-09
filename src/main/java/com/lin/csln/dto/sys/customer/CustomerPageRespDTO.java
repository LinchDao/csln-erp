package com.lin.csln.dto.sys.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 客户分页响应
 */
@Data
@Schema(name = "CustomerPageRespDTO", description = "客户分页响应数据")
public class CustomerPageRespDTO {

    @Schema(description = "客户ID")
    private String id;

    @Schema(description = "客户名称")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "等级ID")
    private String levelId;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "状态：1-启用，0-禁用")
    private Integer status;

    @Schema(description = "创建时间")
    private String createTime;
}
