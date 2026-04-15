package com.lin.csln.service.impl;

import com.lin.csln.entity.LogDO;
import com.lin.csln.mapper.LogMapper;
import com.lin.csln.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统操作日志 服务实现类
 */
@Slf4j
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLogIgnoreDuplicate(LogDO logDO) {
        try {
            this.save(logDO);
        } catch (DuplicateKeyException ex) {
            log.debug("操作日志重复消费，忽略重复插入。logId={}", logDO == null ? null : logDO.getId());
        }
    }
}