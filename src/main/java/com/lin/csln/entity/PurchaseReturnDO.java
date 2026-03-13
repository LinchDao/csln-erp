package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 采购退货单 实体类
 * @author 系统生成器
 */
@Data
@TableName("purchase_return")
public class PurchaseReturnDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 退货单号
     */
    private String returnNo;

    /**
     * 采购单ID
     */
    private Long purchaseId;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 退货仓库
     */
    private Long warehouseId;

    /**
     * 0待审核 1已审核
     */
    private Integer status;

    /**
     * 退货时间
     */
    private Date createTime;

}
