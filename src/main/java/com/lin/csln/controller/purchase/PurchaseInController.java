package com.lin.csln.controller.purchase;


import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.dto.Result;
import com.lin.csln.dto.purchase.in.*;
import com.lin.csln.service.PurchaseInService;
import com.lin.csln.service.UserService;
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
@RequestMapping("/purchase/in")
@Tag(name = "采购入库单", description = "采购入库单管理接口")
public class PurchaseInController {

    @Resource
    private PurchaseInService purchaseInService;

    @Operation(summary = "创建采购入库单", description = "根据采购单创建入库单，自动生成入库单号")
    @PostMapping("/create")
    public Result<String> createPurchaseIn(@Valid @RequestBody PurchaseInDTO dto) {
        String userId = JwtTokenUtil.getUserId();
        return Result.success(purchaseInService.createPurchaseIn(dto, userId));
    }


    @Operation(summary = "查询采购单已入库数量", description = "查询采购单已入库数量")
    @GetMapping("/instockedQty/{purchaseId}")
    public Result<List<PurchaseInstockedQtyDTO>> listInstockedQty(
            @Parameter(description = "采购单ID") @PathVariable String purchaseId
    ) {
        return Result.success(purchaseInService.listInstockedQty(purchaseId));
    }

    @Operation(summary = "查询是否存在未审核/未通过的入库单")
    @GetMapping("/checkUnaudit/{purchaseId}")
    public Result<Boolean> checkExistUnAudit(
            @Parameter(description = "采购单ID") @PathVariable String purchaseId
    ) {
        return Result.success(purchaseInService.checkExistUnAudit(purchaseId));
    }

    @Operation(summary = "采购入库单分页查询")
    @PostMapping("/page")
    public Result<PageRespDTO<PurchaseInPageRespDTO>> page(@RequestBody PurchaseInQueryParamDTO dto) {
        return Result.success(purchaseInService.pagePurchaseIn(dto));
    }

    @Operation(summary = "入库单审核通过", description = "审核通过：状态0 → 1")
    @PutMapping("/audit/pass/{inId}")
    public Result<String> auditPass(
            @Parameter(description = "入库单ID") @PathVariable String inId
    ) {
        purchaseInService.auditPass(inId, JwtTokenUtil.getUserId());
        return Result.success("审核通过成功");
    }

    @Operation(summary = "入库单审核驳回", description = "审核驳回：状态0 → 0（仅记录操作）")
    @PutMapping("/audit/reject/{inId}")
    public Result<String> auditReject(
            @Parameter(description = "入库单ID") @PathVariable String inId
    ) {
        purchaseInService.auditReject(inId, JwtTokenUtil.getUserId());
        return Result.success("审核驳回成功");
    }

    @Operation(summary = "采购入库单详情查询")
    @GetMapping("/{inId}")
    public Result<PurchaseInDetailRespDTO> detail(
            @Parameter(description = "入库单主键ID", required = true) @PathVariable String inId
    ) {
        return Result.success(purchaseInService.getPurchaseInDetail(inId));
    }



}
