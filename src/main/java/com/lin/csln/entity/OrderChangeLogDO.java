package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 订单修改日志 实体类
 * @author 系统生成器
 */
@Data
@TableName("order_change_log")
public class OrderChangeLogDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 母单ID
     */
    private String masterId;

    /**
     * 子单ID
     */
    private String subId;

    /**
     * 修改前
     */
    private String beforeContent;

    /**
     * 修改后
     */
    private String afterContent;

    /**
     * 申请人
     */
    private String applyUserId;

    /**
     * 审批人
     */
    private String approveUserId;

    /**
     * 0待审批 1通过 2拒绝
     */
    private Integer status;

    /**
     * 操作时间
     */
    private Date createTime;

}
