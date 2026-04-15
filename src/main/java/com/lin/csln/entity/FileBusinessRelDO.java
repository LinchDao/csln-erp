package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 文件-业务关联表（解耦文件与业务，支持一对多关联） 实体类
 * @author 系统生成器
 */
@Data
@TableName("sys_file_business_rel")
public class FileBusinessRelDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 关联文件ID（关联sys_file.id）
     */
    private String fileId;

    /**
     * 业务类型（枚举BusinessTypeEnum，如product_main_image）
     */
    private String businessType;

    /**
     * 关联业务主键ID（如商品ID/订单ID）
     */
    private String businessId;

    /**
     * 排序号（同业务下文件排序）
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除（0-未删，1-已删）
     */
    private Integer isDelete;

}
