package com.lin.csln.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("product_dim_image")
public class ProductDimImageDO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String productId;

    private String dimKey;

    private String dimValue;

    private String fileId;

    private Date createTime;

    private Integer isDelete;
}
