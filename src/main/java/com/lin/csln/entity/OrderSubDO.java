package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 子订单表 实体类
 * @author 系统生成器
 */
@Data
@TableName("order_sub")
public class OrderSubDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 子订单号
     */
    private String subOrderNo;

    /**
     * 母单ID
     */
    private String masterId;

    /**
     * 发货仓库
     */
    private String warehouseId;

    /**
     * 子单金额
     */
    private BigDecimal amount;

    /**
     * 1本仓 2协发 3调货
     */
    private Integer sendType;

    /**
     * 来源仓
     */
    private String fromWarehouseId;

    /**
     * 子单状态
     */
    private Integer status;

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
     * 配送备注
     */
    private String deliveryRemark;

    /**
     * 期望发货日期
     */
    private Date expectSendDate;

    /**
     * 实际发货日期
     */
    private Date actualSendDate;

    /**
     * 配货员
     */
    private String pickerUserId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private Integer isDelete;

}
