package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;

/**
 * SKU表 实体类
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
     * 颜色ID
     */
    private String colorId;

    /**
     * 尺码ID
     */
    private String sizeId;

    /**
     * 条码
     */
    private String barcode;

}
