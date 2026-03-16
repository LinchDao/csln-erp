package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;

/**
 * 客户专属价格表 实体类
 * @author 系统生成器
 */
@Data
@TableName("price_customer")
public class PriceCustomerDO {

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
     * 客户ID
     */
    private String customerId;

    /**
     * 价格
     */
    private BigDecimal price;

}
