package com.lin.csln.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 系统数据字典表 实体类
 * @author 系统生成器
 */
@Data
@TableName("sys_dict")
public class DictDO {

    /**
     * 字典主键
     */
    private String id;

    /**
     * 父级ID，顶级节点为0
     */
    private String parentId;

    /**
     * 字典名称/标签
     */
    private String dictName;

    /**
     * 字典值（存储实际使用的编码/值）
     */
    private String dictValue;

    /**
     * 排序号，数字越小越靠前
     */
    private Integer sort;

    /**
     * 状态：1-启用 0-禁用
     */
    private Integer status;

    /**
     * 删除标识：0-未删除 1-已删除
     */
    private Integer isDelete;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
