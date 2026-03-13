package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 颜色表 实体类
 * @author 系统生成器
 */
@Data
@TableName("color")
public class ColorDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 颜色名称
     */
    private String name;

    /**
     * 颜色编码
     */
    private String code;

    /**
     * 排序
     */
    private Integer sort;

}
