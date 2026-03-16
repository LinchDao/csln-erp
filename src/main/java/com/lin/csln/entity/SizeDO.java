package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 尺码表 实体类
 * @author 系统生成器
 */
@Data
@TableName("size")
public class SizeDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 尺码名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

}
