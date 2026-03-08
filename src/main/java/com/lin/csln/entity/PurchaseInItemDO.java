package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;

/**
 * 入库明细表 实体类
 * @author 系统生成器
 */
@Data
@TableName("purchase_in_item")
public class PurchaseInItemDO {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 入库单ID
     */
    private Long inId;

    /**
     * SKU_ID
     */
    private Long skuId;

    /**
     * 入库数量
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
