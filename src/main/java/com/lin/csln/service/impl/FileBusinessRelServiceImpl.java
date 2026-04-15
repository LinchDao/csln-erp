package com.lin.csln.service.impl;

import com.lin.csln.entity.FileBusinessRelDO;
import com.lin.csln.mapper.FileBusinessRelMapper;
import com.lin.csln.service.FileBusinessRelService;
import com.lin.csln.service.impl.BaseReadonlyServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 文件-业务关联表（解耦文件与业务，支持一对多关联） 服务实现类
 * @author 系统生成器
 */
@Service
public class FileBusinessRelServiceImpl extends BaseReadonlyServiceImpl<FileBusinessRelMapper, FileBusinessRelDO> implements FileBusinessRelService {

}
