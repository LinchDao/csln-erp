package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 调货明细表 实体类
 * @author 系统生成器
 */
@Data
@TableName("stock_transfer_item")
public class StockTransferItemDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 调货单ID
     */
    private String transferId;

    /**
     * SKU_ID
     */
    private String skuId;

    /**
     * 申请数量
     */
    private Integer qty;

    /**
     * 发出数量
     */
    private Integer sendQty;

    /**
     * 收货数量
     */
    private Integer receiveQty;

    /**
     * 差异数量
     */
    private Integer diffQty;

    /**
     * 0无 1少货 2破损 3多货
     */
    private Integer diffType;

    /**
     * 差异备注
     */
    private String diffRemark;

}
