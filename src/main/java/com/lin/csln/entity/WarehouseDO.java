package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 仓库表 实体类
 * @author 系统生成器
 */
@Data
@TableName("warehouse")
public class WarehouseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 仓管ID
     */
    private Long managerUserId;

    /**
     * 地址
     */
    private String address;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

}
