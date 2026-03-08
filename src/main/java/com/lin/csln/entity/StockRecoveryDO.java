package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 追回货表 实体类
 * @author 系统生成器
 */
@Data
@TableName("stock_recovery")
public class StockRecoveryDO {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * SKU_ID
     */
    private Long skuId;

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * 数量
     */
    private Integer qty;

    /**
     * 1待换货 2退货未入仓 3作废未追回
     */
    private Integer type;

    /**
     * 来源单号
     */
    private String sourceOrderNo;

    /**
     * 0未占用 1已占用
     */
    private Integer status;

    /**
     * 占用子单ID
     */
    private Long usedSubOrderId;

    /**
     * 占用时间
     */
    private Date usedTime;

}
