package com.lin.csln.service;

import com.lin.csln.entity.FileDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 文件核心信息表（存储文件基础信息，与业务解耦） 服务接口
 * @author 系统生成器
 */
public interface FileService extends IService<FileDO> {

    long saveFile(String fileName, String uniqueFileName, String fileExt, String dateDir, String string, long size, String contentType, Long userId);
}
