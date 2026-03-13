package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单主表 实体类
 * @author 系统生成器
 */
@Data
@TableName("order_master")
public class OrderMasterDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 门店ID
     */
    private Long shopId;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 总数量
     */
    private Integer totalQty;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 1现货 2预售 3分批发货
     */
    private Integer orderType;

    /**
     * 1专属 2等级 3上次 4手动
     */
    private Integer priceFrom;

    /**
     * 0无需 1待审核 2通过 3拒绝
     */
    private Integer auditStatus;

    /**
     * 审核人
     */
    private Long auditUserId;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 业绩归属销售ID
     */
    private Long salesUserId;

    /**
     * 1快递 2司机 3自提
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
     * 开单时间
     */
    private Date createTime;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 开单人
     */
    private Long createUserId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否生成应收 0否 1是
     */
    private Integer isAr;

    /**
     * 是否允许改码凑单 0不允许 1允许
     */
    private Integer allowReplace;

    /**
     * 是否草稿 0正式单 1草稿单
     */
    private Integer isDraft;

}
