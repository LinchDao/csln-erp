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
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 子订单号
     */
    private String subOrderNo;

    /**
     * 母单ID
     */
    private Long masterId;

    /**
     * 发货仓库
     */
    private Long warehouseId;

    /**
     * 子单数量
     */
    private Integer qty;

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
    private Long fromWarehouseId;

    /**
     * 子单状态
     */
    private Integer status;

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
    private Long pickerUserId;

    /**
     * 备注
     */
    private String remark;

}
