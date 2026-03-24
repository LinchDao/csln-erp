package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.csln.entity.FileDO;
import com.lin.csln.mapper.FileMapper;
import com.lin.csln.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 文件核心信息表（存储文件基础信息，与业务解耦） 服务实现类
 *
 * @author 系统生成器
 */
@Service
@Transactional(readOnly = true)
public class FileServiceImpl extends ServiceImpl<FileMapper, FileDO> implements FileService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveFile(String fileName, String uniqueFileName, String fileExt,
                           String dateDir, String fullpath, long size,
                           String contentType, String userId) {
        FileDO file = new FileDO();

        file.setFileName(fileName);
        file.setStorageFileName(uniqueFileName);
        file.setFilePath(dateDir);
        file.setFullFilePath(fullpath);
        file.setFileSize(size);
        file.setFileExtension(fileExt);
        file.setContentType(contentType);
        file.setCreateTime(new Date());
        file.setCreateUser(userId);
        file.setIsDelete(0);
        baseMapper.insert(file);
        return file.getId();
    }


}