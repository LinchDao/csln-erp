package com.lin.csln.controller.sys;

import com.lin.csln.common.dto.Result;
import com.lin.csln.dto.sys.role.RoleListDTO;
import com.lin.csln.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Author: linch
 */
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@Tag(name = "角色管理", description = "角色管理")
public class RoleController {

    @Resource
    private RoleService roleService;

    @Operation(summary = "查询所有角色（下拉专用）")
    @GetMapping("/list")
    public Result<List<RoleListDTO>> list() {
        return Result.success(roleService.listRoleForSelect());
    }
}