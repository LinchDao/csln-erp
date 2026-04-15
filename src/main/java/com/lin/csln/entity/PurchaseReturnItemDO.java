package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;

/**
 * 退货明细表 实体类
 * @author 系统生成器
 */
@Data
@TableName("purchase_return_item")
public class PurchaseReturnItemDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 退货单ID
     */
    private String returnId;

    /**
     * SKU_ID
     */
    private String skuId;

    /**
     * 退货数量
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
