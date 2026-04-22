package com.lin.csln.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.dto.sys.log.LogPageRespDTO;
import com.lin.csln.dto.sys.log.LogQueryParamDTO;
import com.lin.csln.entity.LogDO;

/**
 * 系统操作日志 服务接口
 */
public interface LogService extends IService<LogDO> {

    void saveLog(LogDO logDO);

    void saveFailLog(LogDO logDO);

    void saveLogIgnoreDuplicate(LogDO logDO);

    PageRespDTO<LogPageRespDTO> pageLog(LogQueryParamDTO queryDTO);
}
