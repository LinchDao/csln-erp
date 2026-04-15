package com.lin.csln.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * SKU表 实体类
 *
 * @author 系统生成器
 */
@Data
@TableName("product_sku")
public class ProductSkuDO {

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
     * 颜色
     */
    private String colorName;

    /**
     * 尺码
     */
    private String sizeName;

    /**
     * 条码
     */
    private String barcode;
    private Integer isDelete;

}
