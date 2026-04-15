package com.lin.csln.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 商品颜色图片表 实体类
 *
 * @author 系统生成器
 */
@Data
@TableName("product_color_image")
public class ProductColorImageDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 颜色ID
     */
    private String colorName;

    /**
     * 颜色图片文件ID（关联file表）
     */
    private String colorFileId;

    /**
     * 创建时间
     */
    private Date createTime;
    private Integer isDelete;

}
