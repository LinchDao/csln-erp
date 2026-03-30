package com.lin.csln.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购订单 实体类
 *
 * @author 系统生成器
 */
@Data
@TableName("purchase_order")
public class PurchaseOrderDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 采购单号
     */
    private String purchaseNo;

    /**
     * 供应商ID
     */
    private String supplierId;

    /**
     * 总数量
     */
    private Integer totalQty;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 0待入库 1部分入库 2已完成 3取消
     */
    private Integer status;

    /**
     * 制单人
     */
    private String createUserId;

    /**
     * 下单时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;
    private Date orderTime;
    private Date arrivalTime;
    private Integer isDelete;

}
