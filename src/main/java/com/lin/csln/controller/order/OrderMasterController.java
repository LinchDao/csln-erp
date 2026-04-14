package com.lin.csln.controller.order;

import com.lin.csln.common.auth.annotation.RequirePermission;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.dto.Result;
import com.lin.csln.log.annotation.OperationLog;
import com.lin.csln.dto.order.CustomerDTO;
import com.lin.csln.dto.order.OrderMasterDetailRespDTO;
import com.lin.csln.dto.order.OrderMasterDTO;
import com.lin.csln.dto.order.OrderMasterPageRespDTO;
import com.lin.csln.dto.order.OrderMasterQueryParamDTO;
import com.lin.csln.enums.BizIdSourceEnum;
import com.lin.csln.enums.OperationLogActionEnum;
import com.lin.csln.enums.OperationLogModuleEnum;
import com.lin.csln.enums.PermissionGateEnum;
import com.lin.csln.service.CustomerService;
import com.lin.csln.service.OrderMasterService;
import com.lin.csln.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order/master")
@Tag(name = "订单主单", description = "订单主单分页、新建、修改接口")
public class OrderMasterController {

    @Resource
    private OrderMasterService orderMasterService;

    @PostMapping("/page")
    @Operation(summary = "订单主单分页查询")
    public Result<PageRespDTO<OrderMasterPageRespDTO>> page(@RequestBody OrderMasterQueryParamDTO queryParamDTO) {
        return Result.success(orderMasterService.pageOrderMaster(queryParamDTO));
    }

    @PostMapping("/create")
    @RequirePermission({PermissionGateEnum.ORDER_MASTER_CREATE})
    @Operation(summary = "创建订单主单")
    @OperationLog(
            module = OperationLogModuleEnum.ORDER_MASTER,
            actionType = OperationLogActionEnum.CREATE,
            bizIdSource = BizIdSourceEnum.RESULT_DATA,
            bizIdField = "id"
    )
    public Result<String> create(@RequestBody OrderMasterDTO dto) {
        String userId = JwtTokenUtil.getUserId();
        return Result.success(orderMasterService.createOrderMaster(dto, userId));
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "订单主单详情")
    public Result<OrderMasterDetailRespDTO> detail(@Parameter(description = "订单主单ID", required = true) @PathVariable String id) {
        return Result.success(orderMasterService.getOrderMasterDetail(id));
    }

    @PutMapping("/{id}")
    @RequirePermission({PermissionGateEnum.ORDER_MASTER_UPDATE})
    @Operation(summary = "编辑正式单")
    @OperationLog(
            module = OperationLogModuleEnum.ORDER_MASTER,
            actionType = OperationLogActionEnum.UPDATE,
            bizIdSource = BizIdSourceEnum.PATH_VARIABLE,
            bizIdField = "id"
    )
    public Result<Void> edit(@Parameter(description = "订单主单ID", required = true) @PathVariable String id,
                             @RequestBody OrderMasterDTO dto) {
        orderMasterService.editOrderMaster(id, dto);
        return Result.success();
    }

    @PutMapping("/{id}/draft")
    @RequirePermission({PermissionGateEnum.ORDER_MASTER_UPDATE})
    @Operation(summary = "编辑草稿单")
    @OperationLog(
            module = OperationLogModuleEnum.ORDER_MASTER,
            actionType = OperationLogActionEnum.UPDATE,
            bizIdSource = BizIdSourceEnum.PATH_VARIABLE,
            bizIdField = "id"
    )
    public Result<Void> editDraft(@Parameter(description = "订单主单ID", required = true) @PathVariable String id,
                                  @RequestBody OrderMasterDTO dto) {
        orderMasterService.editDraftOrderMaster(id, dto);
        return Result.success();
    }

    @PostMapping("/{id}/submit")
    @RequirePermission({PermissionGateEnum.ORDER_MASTER_UPDATE})
    @Operation(summary = "草稿单提交（可带最终内容）")
    @OperationLog(
            module = OperationLogModuleEnum.ORDER_MASTER,
            actionType = OperationLogActionEnum.UPDATE,
            bizIdSource = BizIdSourceEnum.PATH_VARIABLE,
            bizIdField = "id"
    )
    public Result<Void> submit(@Parameter(description = "订单主单ID", required = true) @PathVariable String id,
                               @RequestBody(required = false) OrderMasterDTO dto) {
        orderMasterService.submitOrderMaster(id, dto);
        return Result.success();
    }
}
