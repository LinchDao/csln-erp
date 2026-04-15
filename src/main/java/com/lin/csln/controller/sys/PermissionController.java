package com.lin.csln.controller.sys;

import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.dto.Result;
import com.lin.csln.dto.sys.permission.PermissionListDTO;
import com.lin.csln.enums.BizIdSourceEnum;
import com.lin.csln.enums.OperationLogActionEnum;
import com.lin.csln.enums.OperationLogModuleEnum;
import com.lin.csln.log.annotation.OperationLog;
import com.lin.csln.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Operation(summary = "分页查询权限")
    @PostMapping("/page")
    public Result<PageRespDTO<PermissionListDTO>> pagePermission(@RequestBody(required = false) PermissionListDTO queryDTO) {
        return Result.success(permissionService.pagePermission(queryDTO));
    }

    @Operation(summary = "查询权限详情")
    @GetMapping("/get/{id}")
    public Result<PermissionListDTO> getPermission(@PathVariable String id) {
        return Result.success(permissionService.getPermissionDetail(id));
    }

    @Operation(summary = "新增权限")
    @PostMapping("/add")
    @OperationLog(
            module = OperationLogModuleEnum.ROLE,
            actionType = OperationLogActionEnum.CREATE,
            bizIdSource = BizIdSourceEnum.RESULT_DATA,
            bizIdField = "id"
    )
    public Result<String> addPermission(@Valid @RequestBody PermissionListDTO permission) {
        return Result.success(permissionService.addPermission(permission));
    }

    @Operation(summary = "修改权限")
    @PutMapping("/update")
    @OperationLog(
            module = OperationLogModuleEnum.ROLE,
            actionType = OperationLogActionEnum.UPDATE,
            bizIdSource = BizIdSourceEnum.REQUEST_BODY,
            bizIdField = "id"
    )
    public Result<Void> updatePermission(@Valid @RequestBody PermissionListDTO permission) {
        permissionService.updatePermission(permission);
        return Result.success();
    }

    @Operation(summary = "删除权限")
    @DeleteMapping("/delete/{id}")
    @OperationLog(
            module = OperationLogModuleEnum.ROLE,
            actionType = OperationLogActionEnum.DELETE,
            bizIdSource = BizIdSourceEnum.PATH_VARIABLE,
            bizIdField = "id"
    )
    public Result<Void> deletePermission(@PathVariable String id) {
        permissionService.deletePermission(id);
        return Result.success();
    }
}