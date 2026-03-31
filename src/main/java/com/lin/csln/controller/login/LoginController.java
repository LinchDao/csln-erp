package com.lin.csln.controller.login;

import com.lin.csln.common.constants.ResultCode;
import com.lin.csln.common.dto.Result;
import com.lin.csln.config.AppSecurityProperties;
import com.lin.csln.dto.login.LoginReqDTO;
import com.lin.csln.entity.UserDO;
import com.lin.csln.service.UserService;
import com.lin.csln.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.commons.codec.digest.DigestUtils;
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
    private UserService userService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private AppSecurityProperties securityProperties;


    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "账号密码登录，返回JWT Token")
    public Result<String> login(@Valid @RequestBody LoginReqDTO loginRequest) {

        UserDO user = userService.getUserByUsername(loginRequest.getUsername());
        if (user == null) {
            return Result.fail(ResultCode.USER_NOT_EXIST);
        }

        String salt = securityProperties.getPasswordSalt();
        boolean saltEnabled = securityProperties.getSaltEnabled();
        String encryptPwd;
        if (saltEnabled) {
            encryptPwd = DigestUtils.md5Hex(loginRequest.getPassword() + salt);
        } else {
            encryptPwd = DigestUtils.md5Hex(loginRequest.getPassword());
        }

        if (!encryptPwd.equals(user.getPassword())) {
            return Result.fail(ResultCode.PASSWORD_ERROR);
        }

        if (user.getStatus() == 0) {
            return Result.fail(ResultCode.USER_DISABLED);
        }

        String token = jwtTokenUtil.generateToken(user.getId().toString(), loginRequest.getRememberMe());

        return Result.success("登录成功", token);
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录", description = "通知客户端清理本地Token；服务端不做Token吊销")
    public Result<String> logout() {
        // 当前系统使用无状态JWT，登出由客户端删除本地token实现
        return Result.success("退出登录成功");
    }
}
