package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 协发单 实体类
 * @author 系统生成器
 */
@Data
@TableName("order_help_send")
public class OrderHelpSendDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 子单ID
     */
    private String orderSubId;

    /**
     * 代发仓库
     */
    private String fromWareId;

    /**
     * 状态
     */
    private Integer status;

}
