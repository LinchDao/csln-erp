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
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 退换货单ID
     */
    private String asId;

    /**
     * 原订单明细ID
     */
    private String orderItemId;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 颜色ID
     */
    private String colorId;

    /**
     * 原真实SKU（参考）
     */
    private String realSkuId;

    /**
     * 原发货SKU
     */
    private String shipSkuId;

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
    private String newSkuId;

    /**
     * 换货发出数量
     */
    private Integer newQty;

    /**
     * 仓库ID
     */
    private String warehouseId;

    /**
     * 仓管确认的真实SKU（最终入库用）
     */
    private String confirmRealSkuId;

    /**
     * 创建时间
     */
    private Date createTime;

}
