package com.lin.csln.controller.sys;


import com.lin.csln.common.dto.Result;
import com.lin.csln.dto.stock.warehouse.WarehouseListDTO;
import com.lin.csln.service.WarehouseService;
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
@RequestMapping("/warehouse")
@RequiredArgsConstructor
@Tag(name = "仓库管理", description = "仓库管理")
public class WarehouseController {

    @Resource
    private WarehouseService warehouseService;

    @Operation(summary = "查询所有仓库（下拉专用）")
    @GetMapping("/list")
    public Result<List<WarehouseListDTO>> list() {
        return Result.success(warehouseService.listWarehouseForSelect());
    }

}
