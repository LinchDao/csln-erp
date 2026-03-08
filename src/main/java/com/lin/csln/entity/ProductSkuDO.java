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
    @TableId(type = IdType.AUTO)
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
     * 尺码ID
     */
    private Long sizeId;

    /**
     * 条码
     */
    private String barcode;

}
