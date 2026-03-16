package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 文件核心信息表（存储文件基础信息，与业务解耦） 实体类
 * @author 系统生成器
 */
@Data
@TableName("sys_file")
public class FileDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 文件原始名称
     */
    private String fileName;

    /**
     * 服务器存储文件名（防重复，如：202603111530_8888.jpg）
     */
    private String storageFileName;

    /**
     * 文件存储路径（相对路径，如：/upload/2026/03/11/）
     */
    private String filePath;

    /**
     * 文件完整存储路径（绝对路径，如：/data/upload/2026/03/11/202603111530_8888.jpg）
     */
    private String fullFilePath;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件扩展名（小写，如jpg/png/pdf）
     */
    private String fileExtension;

    /**
     * 文件MIME类型（如image/jpeg/application/pdf）
     */
    private String contentType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人ID
     */
    private String createUser;

    /**
     * 是否删除（0-未删，1-已删）
     */
    private Integer isDelete;

}
