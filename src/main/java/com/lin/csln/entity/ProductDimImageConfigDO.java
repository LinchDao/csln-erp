package com.lin.csln.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("product_dim_image_config")
public class ProductDimImageConfigDO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String productId;

    private String dimKey;

    private Date createTime;

    private Integer isDelete;
}
