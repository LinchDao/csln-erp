package com.lin.csln.controller.user;


import com.lin.csln.common.constants.ResultCode;
import com.lin.csln.common.dto.Result;
import com.lin.csln.dto.user.UserInfoDTO;
import com.lin.csln.service.UserService;
import com.lin.csln.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 用户信息
 * @Author: linch
 */

@RestController
@Tag(name = "用户信息", description = "用户信息相关接口")
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    @GetMapping("/info")
    public Result<UserInfoDTO> getUserInfo() {

        Long userId = JwtTokenUtil.getUserId();
        if (userId != null) {
            UserInfoDTO userInfoDTO = userService.getUserInfoById(userId);
            return Result.success(userInfoDTO);
        }
        return Result.fail(ResultCode.UNAUTHORIZED);


    }


}
