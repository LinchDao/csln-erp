package com.lin.csln.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "子订单配货完成参数")
public class OrderSubPickingCompleteDTO {

    @NotBlank(message = "子订单ID不能为空")
    @Schema(description = "子订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderSubId;
}
