package com.lin.csln.controller.sys;

import com.lin.csln.common.auth.annotation.RequirePermission;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.dto.Result;
import com.lin.csln.dto.sys.role.RoleDetailRespDTO;
import com.lin.csln.dto.sys.role.RoleListDTO;
import com.lin.csln.dto.sys.role.RolePageRespDTO;
import com.lin.csln.dto.sys.role.RoleQueryParamDTO;
import com.lin.csln.dto.sys.role.RoleSaveReqDTO;
import com.lin.csln.enums.BizIdSourceEnum;
import com.lin.csln.enums.OperationLogActionEnum;
import com.lin.csln.enums.OperationLogModuleEnum;
import com.lin.csln.enums.PermissionGateEnum;
import com.lin.csln.log.annotation.OperationLog;
import com.lin.csln.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/page")
    @Operation(summary = "分页查询角色")
    public Result<PageRespDTO<RolePageRespDTO>> pageRole(@RequestBody RoleQueryParamDTO queryDTO) {
        return Result.success(roleService.pageRole(queryDTO));
    }

    @PostMapping("/add")
    @Operation(summary = "新增角色（可配置菜单与权限）")
    @RequirePermission({PermissionGateEnum.ROLE_CREATE})
    @OperationLog(
            module = OperationLogModuleEnum.ROLE,
            actionType = OperationLogActionEnum.CREATE,
            bizIdSource = BizIdSourceEnum.RESULT_DATA,
            bizIdField = "id"
    )
    public Result<String> addRole(@RequestBody RoleSaveReqDTO dto) {
        return Result.success(roleService.addRole(dto));
    }

    @PutMapping("/update")
    @Operation(summary = "修改角色（可配置菜单与权限）")
    @RequirePermission({PermissionGateEnum.ROLE_UPDATE})
    @OperationLog(
            module = OperationLogModuleEnum.ROLE,
            actionType = OperationLogActionEnum.UPDATE,
            bizIdSource = BizIdSourceEnum.REQUEST_BODY,
            bizIdField = "id"
    )
    public Result<Void> updateRole(@RequestBody RoleSaveReqDTO dto) {
        roleService.updateRole(dto);
        return Result.success();
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "角色详情")
    public Result<RoleDetailRespDTO> getRole(@PathVariable String id) {
        return Result.success(roleService.getRoleDetail(id));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除角色")
    @RequirePermission({PermissionGateEnum.ROLE_DELETE})
    @OperationLog(
            module = OperationLogModuleEnum.ROLE,
            actionType = OperationLogActionEnum.DELETE,
            bizIdSource = BizIdSourceEnum.PATH_VARIABLE,
            bizIdField = "id"
    )
    public Result<Void> deleteRole(@PathVariable String id) {
        roleService.deleteRole(id);
        return Result.success();
    }
}
