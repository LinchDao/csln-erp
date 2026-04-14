package com.lin.csln.controller.purchase;


import com.lin.csln.common.auth.annotation.RequirePermission;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.dto.Result;
import com.lin.csln.log.annotation.OperationLog;
import com.lin.csln.dto.purchase.order.*;
import com.lin.csln.enums.BizIdSourceEnum;
import com.lin.csln.enums.OperationLogActionEnum;
import com.lin.csln.enums.OperationLogModuleEnum;
import com.lin.csln.enums.PermissionGateEnum;
import com.lin.csln.service.PurchaseOrderService;
import com.lin.csln.service.SupplierService;
import com.lin.csln.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Author: linch
 */

@RestController
@RequestMapping("/purchase/order")
@Tag(name = "供应管理", description = "供应管理相关接口")
public class PurchaseController {

    @Resource
    private PurchaseOrderService purchaseOrderService;
    @Resource
    private SupplierService supplierService;

    @PostMapping("/page")
    @Operation(summary = "分页查询采购订单", description = "根据条件分页查询采购订单列表")
    public Result<PageRespDTO<PurchaseOrderPageRespDTO>> pagePurchaseOrder(
            @RequestBody PurchaseOrderQueryParamDTO queryDTO) {
        try {
            return Result.success(purchaseOrderService.pagePurchaseOrder(queryDTO));
        } catch (Exception e) {
            return Result.fail("分页查询采购订单失败：" + e.getMessage());
        }
    }

    @GetMapping("/supplier/list")
    @Operation(summary = "供应商列表", description = "供应商列表")
    public Result<List<SupplierDTO>> list(SupplierQueryDTO queryDTO) {
        return Result.success(supplierService.listSupplier(queryDTO));
    }


    @PostMapping("/create")
    @RequirePermission({PermissionGateEnum.PURCHASE_ORDER_CREATE})
    @Operation(summary = "创建采购单", description = "创建采购单")
    @OperationLog(
            module = OperationLogModuleEnum.PURCHASE_ORDER,
            actionType = OperationLogActionEnum.CREATE,
            bizIdSource = BizIdSourceEnum.RESULT_DATA,
            bizIdField = "id"
    )
    public Result<String> create(@RequestBody PurchaseOrderDTO dto) {
        String userId = JwtTokenUtil.getUserId();
        String id = purchaseOrderService.createPurchaseOrder(dto, userId);
        return Result.success(id);
    }

    @PostMapping("/cancel/{id}")
    @RequirePermission({PermissionGateEnum.PURCHASE_ORDER_UPDATE})
    @OperationLog(
            module = OperationLogModuleEnum.PURCHASE_ORDER,
            actionType = OperationLogActionEnum.UPDATE,
            bizIdSource = BizIdSourceEnum.PATH_VARIABLE,
            bizIdField = "id"
    )
    public Result<String> cancelPurchaseOrder(@PathVariable String id) {
        purchaseOrderService.cancelPurchaseOrder(id);
        return Result.success();
    }

    @PutMapping("/{id}")
    @RequirePermission({PermissionGateEnum.PURCHASE_ORDER_UPDATE})
    @Operation(summary = "修改采购单", description = "根据ID查询采购单主信息+明细列表")
    @OperationLog(
            module = OperationLogModuleEnum.PURCHASE_ORDER,
            actionType = OperationLogActionEnum.UPDATE,
            bizIdSource = BizIdSourceEnum.PATH_VARIABLE,
            bizIdField = "id"
    )
    public Result<Void> edit(@Parameter(description = "商品ID", required = true) @PathVariable String id,
                             @Valid @RequestBody PurchaseOrderDTO dto) {
        purchaseOrderService.editPurchaseOrder(id, dto);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "采购单详情", description = "根据ID查询采购单主信息+明细列表")
    public Result<PurchaseOrderDTO> getPurchaseDetail(@PathVariable String id) {
        PurchaseOrderDTO detail = purchaseOrderService.getPurchaseDetail(id);
        return Result.success(detail);
    }
}


