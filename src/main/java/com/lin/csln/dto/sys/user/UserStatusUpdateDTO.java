package com.lin.csln.dto.sys.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "用户启用/禁用请求对象")
public class UserStatusUpdateDTO {

    @NotBlank(message = "用户ID不能为空")
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态仅支持0或1")
    @Max(value = 1, message = "状态仅支持0或1")
    @Schema(description = "状态：1-启用，0-禁用", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
}
