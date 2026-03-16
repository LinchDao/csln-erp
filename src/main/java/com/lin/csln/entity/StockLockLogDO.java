package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 库存锁定日志表 实体类
 * @author 系统生成器
 */
@Data
@TableName("stock_lock_log")
public class StockLockLogDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 仓库ID
     */
    private String warehouseId;

    /**
     * SKU_ID
     */
    private String skuId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 数量
     */
    private Integer qty;

    /**
     * 1锁定 2释放
     */
    private Integer type;

    /**
     * 操作时间
     */
    private Date createTime;

}
