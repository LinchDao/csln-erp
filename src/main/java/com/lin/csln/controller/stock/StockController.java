package com.lin.csln.controller.stock;

import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.dto.Result;
import com.lin.csln.dto.stock.ProductStockPageRespDTO;
import com.lin.csln.dto.stock.ProductStockQueryParamDTO;
import com.lin.csln.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 库存管理控制器
 */
@RestController
@RequestMapping("/stock")
@Tag(name = "库存管理", description = "库存查询相关接口")
public class StockController {

    @Resource
    private StockService stockService;

    @PostMapping("/page")
    @Operation(summary = "分页查询商品库存", description = "按商品、SKU、仓库维度分页查询库存（V2）")
    public Result<PageRespDTO<ProductStockPageRespDTO>> pageProductStock(
            @RequestBody(required = false) ProductStockQueryParamDTO queryDTO) {
        return Result.success(stockService.pageProductStock(queryDTO));
    }
}
