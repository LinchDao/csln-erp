package com.lin.csln.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 采购入库单 实体类
 *
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

    private String auditUserId;
    private Integer totalQty;

}
