package com.lin.csln.controller.sys;

import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.dto.Result;
import com.lin.csln.dto.sys.log.LogPageRespDTO;
import com.lin.csln.dto.sys.log.LogQueryParamDTO;
import com.lin.csln.entity.LogDO;
import com.lin.csln.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "系统日志", description = "系统操作日志接口")
@RequestMapping("/log")
public class LogController {

    @Resource
    private LogService logService;

    @PostMapping("/page")
    @Operation(summary = "分页查询系统日志")
    public Result<PageRespDTO<LogPageRespDTO>> pageLog(@RequestBody(required = false) LogQueryParamDTO queryDTO) {
        LogQueryParamDTO query = queryDTO == null ? new LogQueryParamDTO() : queryDTO;
        return Result.success(logService.pageLog(query));
    }

    @GetMapping("/diff-data/{id}")
    @Operation(summary = "根据ID获取日志差异数据")
    public Result<String> getDiffDataById(@PathVariable String id) {
        LogDO logDO = logService.getById(id);
        if (logDO == null) {
            return Result.fail("日志不存在");
        }
        return Result.success(logDO.getDiffData());
    }
}
