package com.lin.csln.controller.sys;

import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.dto.Result;
import com.lin.csln.dto.order.CustomerDTO;
import com.lin.csln.dto.sys.customer.CustomerDetailRespDTO;
import com.lin.csln.dto.sys.customer.CustomerPageRespDTO;
import com.lin.csln.dto.sys.customer.CustomerQueryParamDTO;
import com.lin.csln.dto.sys.customer.CustomerSaveReqDTO;
import com.lin.csln.dto.sys.customer.CustomerStatusUpdateDTO;
import com.lin.csln.enums.BizIdSourceEnum;
import com.lin.csln.enums.OperationLogActionEnum;
import com.lin.csln.enums.OperationLogModuleEnum;
import com.lin.csln.log.annotation.OperationLog;
import com.lin.csln.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户管理接口
 */
@RestController
@Tag(name = "客户管理", description = "客户管理相关接口")
@RequestMapping("/customer")
public class CustomerController {

    @Resource
    private CustomerService customerService;

    @PostMapping("/page")
    @Operation(summary = "分页查询客户")
    public Result<PageRespDTO<CustomerPageRespDTO>> pageCustomer(@RequestBody CustomerQueryParamDTO queryDTO) {
        return Result.success(customerService.pageCustomer(queryDTO));
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取客户详情")
    public Result<CustomerDetailRespDTO> getCustomerDetail(@PathVariable String id) {
        return Result.success(customerService.getCustomerDetail(id));
    }

    @PostMapping("/add")
    @Operation(summary = "新增客户")
    @OperationLog(
            module = OperationLogModuleEnum.CUSTOMER,
            actionType = OperationLogActionEnum.CREATE,
            bizIdSource = BizIdSourceEnum.RESULT_DATA,
            bizIdField = "id"
    )
    public Result<String> addCustomer(@Valid @RequestBody CustomerSaveReqDTO dto) {
        return Result.success(customerService.addCustomer(dto));
    }

    @PutMapping("/update")
    @Operation(summary = "修改客户")
    @OperationLog(
            module = OperationLogModuleEnum.CUSTOMER,
            actionType = OperationLogActionEnum.UPDATE,
            bizIdSource = BizIdSourceEnum.REQUEST_BODY,
            bizIdField = "id"
    )
    public Result<Void> updateCustomer(@Valid @RequestBody CustomerSaveReqDTO dto) {
        customerService.updateCustomer(dto);
        return Result.success();
    }

    @PutMapping("/status")
    @Operation(summary = "启用/禁用客户")
    @OperationLog(
            module = OperationLogModuleEnum.CUSTOMER,
            actionType = OperationLogActionEnum.UPDATE,
            bizIdSource = BizIdSourceEnum.REQUEST_BODY,
            bizIdField = "customerId"
    )
    public Result<Void> updateCustomerStatus(@Valid @RequestBody CustomerStatusUpdateDTO dto) {
        customerService.updateCustomerStatus(dto);
        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "客户下拉（仅启用）")
    public Result<List<CustomerDTO>> listCustomerForSelect() {
        return Result.success(customerService.listCustomerForSelect());
    }
}
