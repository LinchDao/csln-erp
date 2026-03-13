package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 发货单 实体类
 * @author 系统生成器
 */
@Data
@TableName("delivery_note")
public class DeliveryNoteDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 发货单号
     */
    private String deliveryNo;

    /**
     * 子单ID
     */
    private Long subOrderId;

    /**
     * 配送方式
     */
    private Integer deliveryType;

    /**
     * 快递单号
     */
    private String expressNo;

    /**
     * 司机/自提手机号
     */
    private String driverPhone;

    /**
     * 备注
     */
    private String remark;

    /**
     * 发货时间
     */
    private Date actualSendTime;

    /**
     * 发货人
     */
    private Long sendUserId;

}
