package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 调货单 实体类
 * @author 系统生成器
 */
@Data
@TableName("stock_transfer")
public class StockTransferDO {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 调货单号
     */
    private String transferNo;

    /**
     * 调出仓
     */
    private Long fromWareId;

    /**
     * 调入仓
     */
    private Long toWareId;

    /**
     * 0待发货 1已发货 2已收货 3取消
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

}
