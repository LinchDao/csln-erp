package com.lin.csln.controller.order;


import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.dto.Result;
import com.lin.csln.dto.order.OrderSubAssignDTO;
import com.lin.csln.dto.order.OrderSubDetailDTO;
import com.lin.csln.dto.order.OrderSubPickingCompleteDTO;
import com.lin.csln.dto.order.OrderSubPageRespDTO;
import com.lin.csln.dto.order.OrderSubQueryParamDTO;
import com.lin.csln.dto.order.OrderSubShipDTO;
import com.lin.csln.service.OrderSubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: linch
 */

@RestController
@RequestMapping("/order/sub")
@Tag(name = "子订单相关接口", description = "子订单相关接口")
public class OrderSubController {

    @Resource
    private OrderSubService orderSubService;


    @PostMapping("/page")
    @Operation(summary = "子订单分页查询")
    public Result<PageRespDTO<OrderSubPageRespDTO>> pageQuery(@RequestBody OrderSubQueryParamDTO queryDTO) {
        return Result.success(orderSubService.pageQuery(queryDTO));
    }

    @PostMapping("/assign")
    @Operation(summary = "子订单分配人员")
    public Result<Void> assignOrderToUser(@Valid @RequestBody OrderSubAssignDTO dto) {
        orderSubService.assignOrderToUser(dto.getOrderSubId(), dto.getUserId());
        return Result.success();
    }

    @PostMapping("/picking/complete")
    @Operation(summary = "子订单配货完成")
    public Result<Void> completePicking(@Valid @RequestBody OrderSubPickingCompleteDTO dto) {
        orderSubService.completePicking(dto);
        return Result.success();
    }

    @PostMapping("/ship")
    @Operation(summary = "子订单发货")
    public Result<Void> ship(@Valid @RequestBody OrderSubShipDTO dto) {
        orderSubService.ship(dto);
        return Result.success();
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "子订单详情")
    public Result<OrderSubDetailDTO> detail(@Parameter(description = "子订单ID", required = true) @PathVariable String id) {
        return Result.success(orderSubService.getDetail(id));
    }

}
