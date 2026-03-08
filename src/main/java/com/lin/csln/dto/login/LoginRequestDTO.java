package com.lin.csln.dto.login;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Description: 登录请求DTO
 * @Author: linch
 */

@Data
@Schema(name = "LoginRequestDTO", description = "登录请求参数")
public class LoginRequestDTO {
    @NotBlank(message = "账号不能为空")
    @Schema(description = "用户名/手机号", example = "admin")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", example = "123456abc")
    private String password;

    @Schema(description = "是否记住我", example = "false", defaultValue = "false")
    private Boolean rememberMe = false;
}