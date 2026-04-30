package com.lin.csln.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("product_v2")
public class ProductV2DO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String productNo;

    private String name;

    private String categoryId;

    private String brand;

    private Integer status;

    private BigDecimal costPrice;

    private BigDecimal wholesalePrice;

    private BigDecimal retailPrice;

    private String mainImageId;

    private String saleAttrsSchema;

    private String extJson;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;
}
