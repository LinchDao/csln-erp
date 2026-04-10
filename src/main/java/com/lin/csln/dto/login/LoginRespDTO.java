package com.lin.csln.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录/续期响应")
public class LoginRespDTO {

    @Schema(description = "访问令牌")
    private String accessToken;

    @Schema(description = "刷新令牌")
    private String refreshToken;

    @Schema(description = "令牌类型", example = "Bearer")
    private String tokenType;

    @Schema(description = "访问令牌过期秒数")
    private Long expiresIn;

    @Schema(description = "兼容旧版字段，等同 accessToken")
    private String token;
}
