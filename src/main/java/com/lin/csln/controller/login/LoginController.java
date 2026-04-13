package com.lin.csln.controller.login;

import com.lin.csln.common.constants.ResultCode;
import com.lin.csln.common.dto.Result;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.log.annotation.OperationLog;
import com.lin.csln.dto.login.LoginReqDTO;
import com.lin.csln.dto.login.LoginRespDTO;
import com.lin.csln.dto.login.RefreshTokenReqDTO;
import com.lin.csln.enums.BizIdSourceEnum;
import com.lin.csln.enums.OperationLogActionEnum;
import com.lin.csln.enums.OperationLogModuleEnum;
import com.lin.csln.service.AuthService;
import com.lin.csln.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: linch
 */


@RestController
@Tag(name = "登录", description = "用户登录相关接口")
public class LoginController {

    @Resource
    private AuthService authService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;


    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "账号密码登录，返回JWT Token")
    @OperationLog(module = OperationLogModuleEnum.AUTH,
            actionType = OperationLogActionEnum.LOGIN,
            bizIdSource = BizIdSourceEnum.REQUEST_BODY,
            bizIdField = "username"
    )
    public Result<LoginRespDTO> login(@Valid @RequestBody LoginReqDTO loginRequest) {
        try {
            return Result.success("登录成功", authService.login(loginRequest));
        } catch (BusinessException ex) {
            return Result.fail(ex.getCode(), ex.getMessage());
        }
    }

    @PostMapping("/auth/refresh")
    @Operation(summary = "刷新令牌", description = "使用refreshToken续期，返回新的accessToken和refreshToken")
    public Result<LoginRespDTO> refresh(@Valid @RequestBody RefreshTokenReqDTO reqDTO) {
        try {
            return Result.success(authService.refresh(reqDTO.getRefreshToken()));
        } catch (BusinessException ex) {
            return Result.fail(ex.getCode(), ex.getMessage());
        } catch (Exception ex) {
            return Result.fail(ResultCode.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录", description = "服务端吊销当前会话并清理Token")
    public Result<String> logout(HttpServletRequest request) {
        String token = jwtTokenUtil.resolveToken(request);
        authService.logout(token);
        return Result.success("退出登录成功");
    }
}
