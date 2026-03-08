package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 库存表 实体类
 * @author 系统生成器
 */
@Data
@TableName("stock")
public class StockDO {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * SKU_ID
     */
    private Long skuId;

    /**
     * 可用库存
     */
    private Integer qty;

    /**
     * 锁定库存
     */
    private Integer lockQty;

    /**
     * 追回货预占
     */
    private Integer recoveryQty;

    /**
     * 预警数量
     */
    private Integer warnQty;

    /**
     * 更新时间
     */
    private Date updateTime;

}
