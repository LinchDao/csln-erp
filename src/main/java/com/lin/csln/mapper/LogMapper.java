package com.lin.csln.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lin.csln.entity.LogDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.csln.dto.sys.log.LogPageRespDTO;
import com.lin.csln.dto.sys.log.LogQueryParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统操作日志 Mapper接口
 * @author 系统生成器
 */
@Mapper
public interface LogMapper extends BaseMapper<LogDO> {

    IPage<LogPageRespDTO> pageLog(IPage<LogPageRespDTO> page, @Param("query") LogQueryParamDTO queryDTO);
}
