package com.lin.csln.controller.product;

import com.lin.csln.common.auth.annotation.RequirePermission;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.dto.Result;
import com.lin.csln.dto.product.ProductDetailV2RespDTO;
import com.lin.csln.dto.product.ProductPageV2RespDTO;
import com.lin.csln.dto.product.ProductV2DTO;
import com.lin.csln.dto.product.ProductV2QueryParamDTO;
import com.lin.csln.enums.BizIdSourceEnum;
import com.lin.csln.enums.OperationLogActionEnum;
import com.lin.csln.enums.OperationLogModuleEnum;
import com.lin.csln.enums.PermissionGateEnum;
import com.lin.csln.log.annotation.OperationLog;
import com.lin.csln.service.ProductV2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 商品管理V2控制器
 */
@RestController
@RequestMapping("/product/v2")
@Tag(name = "商品管理V2接口", description = "商品动态维度SKU的增改查接口")
public class ProductV2Controller {

    @Resource
    private ProductV2Service productV2Service;

    @PostMapping("/add")
    @RequirePermission({PermissionGateEnum.PRODUCT_CREATE})
    @Operation(summary = "新增商品V2", description = "新增商品并显式上传动态维度SKU")
    @OperationLog(module = OperationLogModuleEnum.PRODUCT_V2,
            actionType = OperationLogActionEnum.CREATE,
            bizIdSource = BizIdSourceEnum.RESULT_DATA,
            bizIdField = "id")
    public Result<String> addProductV2(@Valid @RequestBody ProductV2DTO productDTO) {
        try {
            String productId = productV2Service.addProductV2(productDTO);
            return Result.success(productId);
        } catch (Exception e) {
            return Result.fail("新增商品失败：" + e.getMessage());
        }
    }

    @PutMapping("/{productId}")
    @RequirePermission({PermissionGateEnum.PRODUCT_UPDATE})
    @Operation(summary = "修改商品V2", description = "根据ID修改商品并显式上传动态维度SKU")
    @OperationLog(module = OperationLogModuleEnum.PRODUCT_V2,
            actionType = OperationLogActionEnum.UPDATE,
            bizIdSource = BizIdSourceEnum.PATH_VARIABLE,
            bizIdField = "productId")
    public Result<Boolean> updateProductV2(
            @Parameter(description = "商品ID", required = true) @PathVariable String productId,
            @Valid @RequestBody ProductV2DTO productDTO) {
        try {
            boolean success = productV2Service.updateProductV2(productId, productDTO);
            return Result.success(success);
        } catch (Exception e) {
            return Result.fail("修改商品失败：" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询商品详情V2", description = "根据ID查询商品并返回动态维度SKU")
    public Result<ProductDetailV2RespDTO> getProductByIdV2(@Parameter(description = "商品ID", required = true) @PathVariable String id) {
        try {
            ProductDetailV2RespDTO productDTO = productV2Service.getProductByIdV2(id);
            return Result.success(productDTO);
        } catch (Exception e) {
            return Result.fail("查询商品失败：" + e.getMessage());
        }
    }

    @PostMapping("/page")
    @RequirePermission({PermissionGateEnum.PRODUCT_LIST})
    @Operation(summary = "分页查询商品V2", description = "根据条件分页查询V2商品")
    public Result<PageRespDTO<ProductPageV2RespDTO>> pageProductV2(@RequestBody(required = false) ProductV2QueryParamDTO queryDTO) {
        try {
            return Result.success(productV2Service.pageProductV2(queryDTO));
        } catch (Exception e) {
            return Result.fail("分页查询商品失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @RequirePermission({PermissionGateEnum.PRODUCT_DELETE})
    @Operation(summary = "删除商品V2", description = "根据ID删除V2商品（软删）")
    @OperationLog(module = OperationLogModuleEnum.PRODUCT_V2,
            actionType = OperationLogActionEnum.DELETE,
            bizIdSource = BizIdSourceEnum.PATH_VARIABLE,
            bizIdField = "id")
    public Result<Boolean> deleteProductV2(@Parameter(description = "商品ID", required = true) @PathVariable String id) {
        try {
            return Result.success(productV2Service.deleteProductV2(id));
        } catch (Exception e) {
            return Result.fail("删除商品失败：" + e.getMessage());
        }
    }
}
