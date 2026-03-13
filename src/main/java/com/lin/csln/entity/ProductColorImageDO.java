package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 商品颜色图片表 实体类
 * @author 系统生成器
 */
@Data
@TableName("product_color_image")
public class ProductColorImageDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 颜色ID
     */
    private Long colorId;

    /**
     * 颜色图片
     */
    private String image;

    /**
     * 创建时间
     */
    private Date createTime;

}
