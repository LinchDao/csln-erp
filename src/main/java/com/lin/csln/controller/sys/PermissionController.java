package com.lin.csln.controller.sys;

import com.lin.csln.common.dto.Result;
import com.lin.csln.dto.sys.permission.PermissionListDTO;
import com.lin.csln.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "权限接口", description = "权限相关接口")
@RequestMapping("/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    @Operation(summary = "查询权限下拉数据")
    @GetMapping("/list")
    public Result<List<PermissionListDTO>> listPermissionForSelect() {
        return Result.success(permissionService.listPermissionForSelect());
    }
}
