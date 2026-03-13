package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;

/**
 * 采购明细表 实体类
 * @author 系统生成器
 */
@Data
@TableName("purchase_order_item")
public class PurchaseOrderItemDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 采购单ID
     */
    private Long purchaseId;

    /**
     * SKU_ID
     */
    private Long skuId;

    /**
     * 采购数量
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

}
