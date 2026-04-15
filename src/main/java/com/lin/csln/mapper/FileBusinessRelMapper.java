package com.lin.csln.mapper;

import com.lin.csln.entity.FileBusinessRelDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件-业务关联表（解耦文件与业务，支持一对多关联） Mapper接口
 * @author 系统生成器
 */
@Mapper
public interface FileBusinessRelMapper extends BaseMapper<FileBusinessRelDO> {

}
