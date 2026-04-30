package com.lin.csln.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * SKU维度明细表 实体类
 */
@Data
@TableName("product_sku_dim")
public class ProductSkuDimDO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String skuId;

    private String dimKey;

    private String dimName;

    private String dimValue;

    private Integer dimOrder;

    private Integer isDelete;
}
