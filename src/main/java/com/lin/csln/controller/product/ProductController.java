package com.lin.csln.controller.product;


import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.dto.Result;
import com.lin.csln.log.annotation.OperationLog;
import com.lin.csln.enums.BizIdSourceEnum;
import com.lin.csln.dto.product.*;
import com.lin.csln.dto.stock.ProductStockPageRespDTO;
import com.lin.csln.dto.stock.ProductStockQueryParamDTO;
import com.lin.csln.enums.OperationLogActionEnum;
import com.lin.csln.enums.OperationLogModuleEnum;
import com.lin.csln.service.ProductService;
import com.lin.csln.service.ProductSkuService;
import com.lin.csln.service.StockService;
import com.lin.csln.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品管理控制器
 *
 * @Description:
 * @Author: linch
 */


@RestController
@RequestMapping("/product")
@Tag(name = "商品管理接口", description = "商品的增删改查及分页查询接口")
public class ProductController {
    @Resource
    private ProductService productService;
    @Resource
    private ProductSkuService productSkuService;
    @Resource
    private StockService stockService;

    @PostMapping("/add")
    @Operation(summary = "新增商品", description = "新增商品基础信息及关联的颜色图片、SKU信息")
    @OperationLog(
            module = OperationLogModuleEnum.PRODUCT,
            actionType = OperationLogActionEnum.CREATE,
            bizIdSource = BizIdSourceEnum.RESULT_DATA,
            bizIdField = "id"
    )
    public Result<String> addProduct(@Valid @RequestBody ProductDTO productDTO) {
        try {
            String productId = productService.addProduct(productDTO);
            return Result.success(productId);
        } catch (Exception e) {
            return Result.fail("新增商品失败：" + e.getMessage());
        }
    }

    @PutMapping("/{productId}")
    @Operation(summary = "修改商品", description = "根据ID修改商品信息及关联的颜色图片、SKU信息")
    @OperationLog(
            module = OperationLogModuleEnum.PRODUCT,
            actionType = OperationLogActionEnum.UPDATE,
            bizIdSource = BizIdSourceEnum.PATH_VARIABLE,
            bizIdField = "productId"
    )
    public Result<Boolean> updateProduct(
            @Parameter(description = "商品ID", required = true) @PathVariable String productId,
            @Valid @RequestBody ProductDTO productDTO) {
        try {
            boolean success = productService.updateProduct(productId, productDTO);
            return Result.success(success);
        } catch (Exception e) {
            return Result.fail("修改商品失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除商品", description = "根据ID删除商品（级联删除关联的颜色图片、SKU）")
    @OperationLog(
            module = OperationLogModuleEnum.PRODUCT,
            actionType = OperationLogActionEnum.DELETE,
            bizIdSource = BizIdSourceEnum.PATH_VARIABLE,
            bizIdField = "id"
    )
    public Result<Boolean> deleteProduct(@Parameter(description = "商品ID", required = true) @PathVariable String id) {
        try {
            String userId = JwtTokenUtil.getUserId();
            boolean success = productService.deleteProduct(id, userId);
            return Result.success(success);
        } catch (Exception e) {
            return Result.fail("删除商品失败：" + e.getMessage());
        }
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询商品", description = "根据条件分页查询商品列表（不含关联详情）")
    public Result<PageRespDTO<ProductPageRespDTO>> pageProduct(@RequestBody ProductQueryParamDTO queryDTO) {
        try {
            PageRespDTO<ProductPageRespDTO> productPage = productService.pageProduct(queryDTO);
            return Result.success(productPage);
        } catch (Exception e) {
            return Result.fail("分页查询商品失败：" + e.getMessage());
        }
    }

    @PostMapping("/stock/page")
    @Operation(summary = "分页查询商品库存", description = "按商品、SKU、仓库维度分页查询库存")
    public Result<PageRespDTO<ProductStockPageRespDTO>> pageProductStock(@RequestBody(required = false) ProductStockQueryParamDTO queryDTO) {
        return Result.success(stockService.pageProductStock(queryDTO));
    }

    @PostMapping("/export")
    @Operation(summary = "导出商品", description = "根据条件导出商品列表（XLSX）")
    public void exportProduct(@RequestBody(required = false) ProductQueryParamDTO queryDTO,
                              HttpServletResponse response) {
        productService.exportProduct(queryDTO, response);
    }

    @PostMapping("/no/name/list")
    @Operation(summary = "商品款号-名称下拉列表", description = "商品款号-名称下拉列表")
    public Result<List<ProductSelectDTO>> listProduct(
            @RequestBody(required = false) ProductSelectQueryDTO queryDTO) {
        String productNo = queryDTO == null ? null : queryDTO.getProductNo();
        return Result.success(productService.listProductSelect(productNo));
    }

    @GetMapping("/get/sku/by/{productId}")
    @Operation(summary = "商品SKU下拉列表", description = "商品SKU下拉列表")
    public Result<List<ProductSkuListDTO>> getSkuByProductId(@PathVariable String productId) {
        return Result.success(productSkuService.getSkuByProductId(productId));
    }


    @GetMapping("/{id}")
    @Operation(summary = "查询商品详情", description = "根据ID查询商品基础信息及关联的颜色图片、SKU信息")
    public Result<ProductDetailRespDTO> getProductById(@Parameter(description = "商品ID", required = true) @PathVariable String id) {
        try {
            ProductDetailRespDTO productDTO = productService.getProductById(id);
            return Result.success(productDTO);
        } catch (Exception e) {
            return Result.fail("查询商品失败：" + e.getMessage());
        }
    }
}
