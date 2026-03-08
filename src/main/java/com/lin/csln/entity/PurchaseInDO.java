package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 采购入库单 实体类
 * @author 系统生成器
 */
@Data
@TableName("purchase_in")
public class PurchaseInDO {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 入库单号
     */
    private String inNo;

    /**
     * 来源采购单
     */
    private Long purchaseId;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 入库仓库
     */
    private Long warehouseId;

    /**
     * 0待审核 1已审核
     */
    private Integer status;

    /**
     * 操作人
     */
    private Long createUserId;

    /**
     * 入库时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

}
