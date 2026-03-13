package com.lin.csln.mapper;

import com.lin.csln.entity.FileDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件核心信息表（存储文件基础信息，与业务解耦） Mapper接口
 * @author 系统生成器
 */
@Mapper
public interface FileMapper extends BaseMapper<FileDO> {

}
