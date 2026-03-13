package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 最近成交价表 实体类
 * @author 系统生成器
 */
@Data
@TableName("price_last")
public class PriceLastDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 最近成交价
     */
    private BigDecimal lastPrice;

    /**
     * 更新时间
     */
    private Date updateTime;

}
