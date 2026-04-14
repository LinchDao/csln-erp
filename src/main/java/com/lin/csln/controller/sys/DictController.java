package com.lin.csln.controller.sys;


import com.lin.csln.common.auth.annotation.RequirePermission;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.dto.Result;
import com.lin.csln.dto.sys.dict.DictDTO;
import com.lin.csln.dto.sys.dict.DictPageRespDTO;
import com.lin.csln.dto.sys.dict.DictQueryParamDTO;
import com.lin.csln.enums.BizIdSourceEnum;
import com.lin.csln.enums.OperationLogActionEnum;
import com.lin.csln.enums.OperationLogModuleEnum;
import com.lin.csln.enums.PermissionGateEnum;
import com.lin.csln.log.annotation.OperationLog;
import com.lin.csln.service.DictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "数据字典", description = "数据字典相关接口")
@RequestMapping("/dict")
public class DictController {

    @Resource
    private DictService dictService;

    @PostMapping("/add")
    @RequirePermission({PermissionGateEnum.DICT_CREATE})
    @OperationLog(
            module = OperationLogModuleEnum.DICT,
            actionType = OperationLogActionEnum.CREATE,
            bizIdSource = BizIdSourceEnum.RESULT_DATA,
            bizIdField = "id"
    )
    public Result<String> add(@RequestBody DictDTO dict) {
        return Result.success(dictService.addDict(dict));
    }

    @PutMapping("/update")
    @RequirePermission({PermissionGateEnum.DICT_UPDATE})
    @OperationLog(
            module = OperationLogModuleEnum.DICT,
            actionType = OperationLogActionEnum.UPDATE,
            bizIdSource = BizIdSourceEnum.REQUEST_BODY,
            bizIdField = "id"
    )
    public Result<Void> update(@RequestBody DictDTO dict) {
        dictService.updateDict(dict);
        return Result.success();
    }


    @DeleteMapping("/delete/{id}")
    @RequirePermission({PermissionGateEnum.DICT_DELETE})
    @OperationLog(
            module = OperationLogModuleEnum.DICT,
            actionType = OperationLogActionEnum.DELETE,
            bizIdSource = BizIdSourceEnum.PATH_VARIABLE,
            bizIdField = "id"
    )
    public Result<Void> delete(@PathVariable String id) {
        dictService.deleteDict(id);
        return Result.success();
    }

    @DeleteMapping("/batchDelete")
    @RequirePermission({PermissionGateEnum.DICT_DELETE})
    @OperationLog(
            module = OperationLogModuleEnum.DICT,
            actionType = OperationLogActionEnum.DELETE,
            bizIdSource = BizIdSourceEnum.REQUEST_BODY,
            bizIdField = "ids"
    )
    public Result<Void> batchDelete(@RequestBody List<String> ids) {
        dictService.batchDeleteDict(ids);
        return Result.success();
    }

    @GetMapping("/get/{id}")
    public Result<DictDTO> getById(@PathVariable String id) {
        DictDTO dict = dictService.getDictById(id);
        return Result.success(dict);
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询字典", description = "分页查询字典")
    public Result<PageRespDTO<DictPageRespDTO>> pageDict(@RequestBody DictQueryParamDTO queryDTO) {
        try {
            PageRespDTO<DictPageRespDTO> dictPage = dictService.pageDict(queryDTO);
            return Result.success(dictPage);
        } catch (Exception e) {
            return Result.fail("分页查询商品失败：" + e.getMessage());
        }
    }


}
