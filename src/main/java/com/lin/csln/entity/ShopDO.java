package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 门店表 实体类
 * @author 系统生成器
 */
@Data
@TableName("shop")
public class ShopDO {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 门店名称
     */
    private String shopName;

    /**
     * 店长ID
     */
    private Long managerUserId;

    /**
     * 默认送单仓
     */
    private Long defaultWarehouseId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

}
