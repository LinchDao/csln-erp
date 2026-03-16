package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;

/**
 * 客户等级表 实体类
 * @author 系统生成器
 */
@Data
@TableName("customer_level")
public class CustomerLevelDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 等级名称
     */
    private String levelName;

    /**
     * 折扣率
     */
    private BigDecimal discount;

    /**
     * 备注
     */
    private String remark;

}
