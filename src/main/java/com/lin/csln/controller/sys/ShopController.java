package com.lin.csln.controller.sys;

import com.lin.csln.common.dto.Result;
import com.lin.csln.dto.sys.shop.ShopListDTO;
import com.lin.csln.service.ShopService;
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
@RequestMapping("/shop")
@RequiredArgsConstructor
@Tag(name = "门店管理", description = "门店管理")
public class ShopController {

    @Resource
    private ShopService shopService;

    @Operation(summary = "查询所有商店（下拉专用）")
    @GetMapping("/list")
    public Result<List<ShopListDTO>> list() {
        return Result.success(shopService.listShopForSelect());
    }
}