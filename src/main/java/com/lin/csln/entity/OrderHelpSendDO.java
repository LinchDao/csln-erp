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
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 子单ID
     */
    private Long orderSubId;

    /**
     * 代发仓库
     */
    private Long fromWareId;

    /**
     * 状态
     */
    private Integer status;

}
