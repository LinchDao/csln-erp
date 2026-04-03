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
    private String id;

    /**
     * 发货单号
     */
    private String deliveryNo;

    /**
     * 子单ID
     */
    private String subOrderId;

    /**
     * 配送方式 0快递 1即时货运 3自提
     */
    private Integer deliveryType;

    /**
     * 快递单号
     */
    private String expressNo;

    /**
     * 即时货运/自提手机号
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
    private String sendUserId;

}
