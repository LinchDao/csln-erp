package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 收款单 实体类
 * @author 系统生成器
 */
@Data
@TableName("payment")
public class PaymentDO {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 收款单号
     */
    private String paymentNo;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 收款金额
     */
    private BigDecimal amount;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作人
     */
    private Long createUserId;

    /**
     * 收款时间
     */
    private Date createTime;

}
