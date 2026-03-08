package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 退换货明细 实体类
 * @author 系统生成器
 */
@Data
@TableName("after_sale_item")
public class AfterSaleItemDO {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 退换货单ID
     */
    private Long asId;

    /**
     * 原订单明细ID
     */
    private Long orderItemId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 颜色ID
     */
    private Long colorId;

    /**
     * 原真实SKU（参考）
     */
    private Long realSkuId;

    /**
     * 原发货SKU
     */
    private Long shipSkuId;

    /**
     * 退货/换货数量
     */
    private Integer qty;

    /**
     * 1退货 2换货
     */
    private Integer opType;

    /**
     * 换货目标SKU
     */
    private Long newSkuId;

    /**
     * 换货发出数量
     */
    private Integer newQty;

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * 仓管确认的真实SKU（最终入库用）
     */
    private Long confirmRealSkuId;

    /**
     * 创建时间
     */
    private Date createTime;

}
