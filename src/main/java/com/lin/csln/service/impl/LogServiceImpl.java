package com.lin.csln.service.impl;

import com.lin.csln.entity.LogDO;
import com.lin.csln.mapper.LogMapper;
import com.lin.csln.service.LogService;
import com.lin.csln.service.impl.BaseReadonlyServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统操作日志 服务实现类
 * @author 系统生成器
 */
@Service
public class LogServiceImpl extends BaseReadonlyServiceImpl<LogMapper, LogDO> implements LogService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLog(LogDO logDO) {
        this.save(logDO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveFailLog(LogDO logDO) {
        this.save(logDO);
    }
}
