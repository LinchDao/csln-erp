package com.lin.csln.controller.sys;

import com.lin.csln.common.cache.UserCache;
import com.lin.csln.common.constants.ResultCode;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.dto.Result;
import com.lin.csln.common.dto.UserInfoDTO;
import com.lin.csln.dto.sys.user.*;
import com.lin.csln.service.UserService;
import com.lin.csln.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "用户管理", description = "用户相关接口")
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/info")
    @Operation(summary = "获取当前登录用户信息")
    public Result<UserInfoDTO> getUserInfo() {
        String userId = JwtTokenUtil.getUserId();
        if (userId != null) {
            UserInfoDTO userInfoDTO = UserCache.getUserInfo(userId);
            if (userInfoDTO == null) {
                userInfoDTO = userService.getUserInfoById(userId);
                if (userInfoDTO != null) {
                    UserCache.saveUserInfo(userInfoDTO);
                } else {
                    return Result.fail(ResultCode.USER_NOT_EXIST);
                }
            }
            return Result.success(userInfoDTO);
        }
        return Result.fail(ResultCode.UNAUTHORIZED);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询用户列表")
    public Result<PageRespDTO<UserPageRespDTO>> pageUser(@RequestBody UserQueryParamDTO queryDto) {
        return Result.success(userService.pageUser(queryDto));
    }

    @PostMapping("/add")
    @Operation(summary = "添加用户")
    public Result<String> addUser(@Valid @RequestBody UserSaveReqDTO dto) {
        return Result.success(userService.addUser(dto));
    }

    @PutMapping("/update")
    @Operation(summary = "修改用户信息")
    public Result<Void> updateUser(@Valid @RequestBody UserSaveReqDTO dto) {
        userService.updateUser(dto);
        return Result.success();
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取用户详情")
    public Result<UserDetailRespDTO> getUserDetail(@PathVariable String id) {
        return Result.success(userService.getUserDetail(id));
    }

    @PutMapping("/password")
    @Operation(summary = "修改用户密码")
    public Result<Void> changePassword(@Valid @RequestBody UserPasswordUpdateDTO dto) {
        userService.changePassword(dto, JwtTokenUtil.getUserId());
        return Result.success();
    }

    @PutMapping("/password/reset/{userId}")
    @Operation(summary = "重置用户密码")
    public Result<Void> resetUserPassword(@PathVariable String userId) {
        userService.resetUserPassword(userId, JwtTokenUtil.getUserId());
        return Result.success();
    }

    @PutMapping("/status")
    @Operation(summary = "启用/禁用用户")
    public Result<Void> updateUserStatus(@Valid @RequestBody UserStatusUpdateDTO dto) {
        userService.updateUserStatus(dto);
        return Result.success();
    }
}
