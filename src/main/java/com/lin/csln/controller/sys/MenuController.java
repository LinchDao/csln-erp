package com.lin.csln.controller.sys;

import com.lin.csln.common.auth.annotation.RequirePermission;
import com.lin.csln.common.cache.UserCache;
import com.lin.csln.common.constants.ResultCode;
import com.lin.csln.common.dto.Result;
import com.lin.csln.common.dto.UserInfoDTO;
import com.lin.csln.dto.sys.menu.MenuDTO;
import com.lin.csln.dto.sys.menu.MenuListDTO;
import com.lin.csln.dto.sys.menu.MenuSortSaveReqDTO;
import com.lin.csln.enums.PermissionGateEnum;
import com.lin.csln.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: Menu
 * @Author: linch
 */
@RestController
@Tag(name = "Menu API", description = "Menu related endpoints")
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    @GetMapping("/tree")
    public Result<List<MenuDTO>> getMenuTree() {
        UserInfoDTO userInfoDTO = UserCache.getUserInfo();
        if (userInfoDTO == null) {
            return Result.fail(ResultCode.UNAUTHORIZED);
        }
        List<MenuDTO> menuList = menuService.treeMenuByRoleCode(userInfoDTO.getIsAdmin(), userInfoDTO.getRoles());
        return Result.success(menuList);
    }

    @Operation(summary = "Query menu select list")
    @GetMapping("/list")
    public Result<List<MenuListDTO>> listMenuForSelect() {
        return Result.success(menuService.listMenuForSelect());
    }

    @Operation(summary = "保存菜单排序")
    @PutMapping("/sort/save")
    @RequirePermission({PermissionGateEnum.MENU_UPDATE})
    public Result<Void> saveMenuSort(@RequestBody MenuSortSaveReqDTO reqDTO) {
        menuService.saveMenuSort(reqDTO == null ? null : reqDTO.getMenus());
        return Result.success();
    }
}
