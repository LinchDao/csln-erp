package com.lin.csln.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 采购明细表 实体类
 *
 * @author 系统生成器
 */
@Data
@TableName("purchase_order_item")
public class PurchaseOrderItemDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 采购单ID
     */
    private String purchaseId;

    /**
     * SKU_ID
     */
    private String skuId;

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
    private Integer isDelete;

}
