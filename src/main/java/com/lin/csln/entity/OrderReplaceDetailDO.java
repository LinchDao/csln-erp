package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 订单改码配货明细表 实体类
 * @author 系统生成器
 */
@Data
@TableName("order_replace_detail")
public class OrderReplaceDetailDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 订单明细ID
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
     * 客户要的SKU（目标码）
     */
    private String targetSkuId;

    /**
     * 实际发货SKU（真实库存码）
     */
    private String realSkuId;

    /**
     * 数量
     */
    private Integer qty;

    /**
     * 仓库ID
     */
    private String warehouseId;

    /**
     * 创建时间
     */
    private Date createTime;

}
