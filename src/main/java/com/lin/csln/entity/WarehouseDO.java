package com.lin.csln.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 仓库表 实体类
 *
 * @author 系统生成器
 */
@Data
@TableName("warehouse")
public class WarehouseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 仓管ID
     */
    private String managerUserId;

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
    private Integer isDelete;

}
