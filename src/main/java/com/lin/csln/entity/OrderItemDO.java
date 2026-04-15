package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;

/**
 * 订单明细表 实体类
 * @author 系统生成器
 */
@Data
@TableName("order_item")
public class OrderItemDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 母单ID
     */
    private String masterId;

    /**
     * 子单ID
     */
    private String subId;

    /**
     * SKU_ID
     */
    private String skuId;

    /**
     * 下单数量
     */
    private Integer qty;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 实际发货数量
     */
    private Integer actualQty;

}
