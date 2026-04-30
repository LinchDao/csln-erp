package com.lin.csln.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("product_v2_attr")
public class ProductV2AttrDO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String productV2Id;

    private String attrKey;

    private String attrName;

    private String attrValue;

    private String valueType;

    private Integer sort;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;
}
