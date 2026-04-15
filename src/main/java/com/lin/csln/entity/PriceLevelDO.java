package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;

/**
 * 等级价格表 实体类
 * @author 系统生成器
 */
@Data
@TableName("price_level")
public class PriceLevelDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 等级ID
     */
    private String levelId;

    /**
     * 价格
     */
    private BigDecimal price;

}
