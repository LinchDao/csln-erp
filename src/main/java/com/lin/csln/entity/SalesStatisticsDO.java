package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 销售业绩统计表 实体类
 * @author 系统生成器
 */
@Data
@TableName("sales_statistics")
public class SalesStatisticsDO {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 统计日期
     */
    private Date statDate;

    /**
     * day/week/month
     */
    private String statType;

    /**
     * 门店ID
     */
    private Long shopId;

    /**
     * 销售ID
     */
    private Long salesUserId;

    /**
     * 订单数
     */
    private Integer orderCount;

    /**
     * 总数量
     */
    private Integer totalQty;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 总成本
     */
    private BigDecimal costAmount;

    /**
     * 毛利
     */
    private BigDecimal profitAmount;

}
