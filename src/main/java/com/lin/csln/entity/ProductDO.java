package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品表 实体类
 * @author 系统生成器
 */
@Data
@TableName("product")
public class ProductDO {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 款号
     */
    private String productNo;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 季节
     */
    private String season;

    /**
     * 年份
     */
    private String year;

    /**
     * 系列
     */
    private String series;

    /**
     * 成本价
     */
    private BigDecimal costPrice;

    /**
     * 默认批发价
     */
    private BigDecimal wholesalePrice;

    /**
     * 商品主图
     */
    private String image;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

}
