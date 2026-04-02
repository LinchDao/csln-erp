package com.lin.csln.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "客户下拉DTO")
public class CustomerDTO {
    @Schema(description = "客户ID")
    private String id;

    @Schema(description = "客户名称")
    private String name;

    @Schema(description = "客户电话")
    private String phone;
}
