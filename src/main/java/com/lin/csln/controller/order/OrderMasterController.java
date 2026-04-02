package com.lin.csln.controller.order;

import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.dto.Result;
import com.lin.csln.dto.order.CustomerDTO;
import com.lin.csln.dto.order.OrderMasterDetailRespDTO;
import com.lin.csln.dto.order.OrderMasterDTO;
import com.lin.csln.dto.order.OrderMasterPageRespDTO;
import com.lin.csln.dto.order.OrderMasterQueryParamDTO;
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
    @Resource
    private CustomerService customerService;

    @PostMapping("/page")
    @Operation(summary = "订单主单分页查询")
    public Result<PageRespDTO<OrderMasterPageRespDTO>> page(@RequestBody OrderMasterQueryParamDTO queryParamDTO) {
        return Result.success(orderMasterService.pageOrderMaster(queryParamDTO));
    }

    @PostMapping("/create")
    @Operation(summary = "创建订单主单")
    public Result<String> create(@RequestBody OrderMasterDTO dto) {
        String userId = JwtTokenUtil.getUserId();
        return Result.success(orderMasterService.createOrderMaster(dto, userId));
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "订单主单详情")
    public Result<OrderMasterDetailRespDTO> detail(@Parameter(description = "订单主单ID", required = true) @PathVariable String id) {
        return Result.success(orderMasterService.getOrderMasterDetail(id));
    }

    @GetMapping("/customer/list")
    @Operation(summary = "客户下拉列表")
    public Result<List<CustomerDTO>> listCustomer() {
        return Result.success(customerService.listCustomerForSelect());
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑正式单")
    public Result<Void> edit(@Parameter(description = "订单主单ID", required = true) @PathVariable String id,
                             @RequestBody OrderMasterDTO dto) {
        orderMasterService.editOrderMaster(id, dto);
        return Result.success();
    }

    @PutMapping("/{id}/draft")
    @Operation(summary = "编辑草稿单")
    public Result<Void> editDraft(@Parameter(description = "订单主单ID", required = true) @PathVariable String id,
                                  @RequestBody OrderMasterDTO dto) {
        orderMasterService.editDraftOrderMaster(id, dto);
        return Result.success();
    }

    @PostMapping("/{id}/submit")
    @Operation(summary = "草稿单提交（可带最终内容）")
    public Result<Void> submit(@Parameter(description = "订单主单ID", required = true) @PathVariable String id,
                               @RequestBody(required = false) OrderMasterDTO dto) {
        orderMasterService.submitOrderMaster(id, dto);
        return Result.success();
    }
}
