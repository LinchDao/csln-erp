package com.lin.csln.controller.sys;

import com.lin.csln.common.dto.Result;
import com.lin.csln.dto.sys.DailySalesDTO;
import com.lin.csln.dto.sys.GlobalSummaryDTO;
import com.lin.csln.dto.sys.TopCustomerOrderAmountDTO;
import com.lin.csln.dto.sys.TopProductOrderQtyDTO;
import com.lin.csln.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 统计报表接口
 */
@Tag(name = "数据统计报表", description = "统计看板相关接口")
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Resource
    private StatisticsService statisticsService;

    @Operation(summary = "获取全局核心指标概览")
    @GetMapping("/summary")
    public Result<GlobalSummaryDTO> getGlobalSummary() {
        return Result.success(statisticsService.getGlobalSummary());
    }

    @Operation(summary = "获取销售趋势统计图表")
    @GetMapping("/daily-sales")
    public Result<List<DailySalesDTO>> getDailySales(
            @Parameter(description = "查询天数，默认15天")
            @RequestParam(defaultValue = "15") Integer days) {
        return Result.success(statisticsService.getDailySales(days));
    }

    @Operation(summary = "获取近一个月订单量前五商品")
    @GetMapping("/top-products-last-month")
    public Result<List<TopProductOrderQtyDTO>> getTopProductsByOrderQtyInLastMonth() {
        return Result.success(statisticsService.getTopProductsByOrderQtyInLastMonth());
    }

    @Operation(summary = "获取近一个月客户订单金额七及客户商品件数前三")
    @GetMapping("/top-customers-last-month")
    public Result<List<TopCustomerOrderAmountDTO>> getTopCustomersWithTopProductsInLastMonth() {
        return Result.success(statisticsService.getTopCustomersWithTopProductsInLastMonth());
    }
}
