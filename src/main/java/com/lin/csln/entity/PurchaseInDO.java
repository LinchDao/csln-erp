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
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 入库单号
     */
    private String inNo;

    /**
     * 来源采购单
     */
    private String purchaseId;

    /**
     * 供应商ID
     */
    private String supplierId;

    /**
     * 入库仓库
     */
    private String warehouseId;

    /**
     * 0待审核 1已审核
     */
    private Integer status;

    /**
     * 操作人
     */
    private String createUserId;

    /**
     * 入库时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

}
