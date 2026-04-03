package com.lin.csln.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "子订单分配人员参数")
public class OrderSubAssignDTO {

    @NotBlank(message = "子订单ID不能为空")
    @Schema(description = "子订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderSubId;

    @NotBlank(message = "用户ID不能为空")
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;
}
