package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 退换货主表 实体类
 * @author 系统生成器
 */
@Data
@TableName("after_sale")
public class AfterSaleDO {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 退换货单号
     */
    private String asNo;

    /**
     * 原母单ID
     */
    private Long orderId;

    /**
     * 原订单号
     */
    private String orderNo;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 门店ID
     */
    private Long shopId;

    /**
     * 制单销售
     */
    private Long salesUserId;

    /**
     * 1仅退货 2仅换货 3退+换
     */
    private Integer asType;

    /**
     * 0草稿 1待审核 2已审核 3待收货 4已收货 5已完成 6已取消
     */
    private Integer status;

    /**
     * 原因
     */
    private String reason;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

}
