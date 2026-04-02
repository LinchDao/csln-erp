package com.lin.csln.enums;

import lombok.Getter;

/**
 * 主单状态枚举
 */
@Getter
public enum OrderMasterStatusEnums {

    WAIT_AUDIT(0, "待审核"),
    WAREHOUSE_PREPARING(2, "仓库准备中"),
    PART_SHIPPED(3, "部分已发货"),
    FINISHED(5, "订单已完成"),
    DRAFT(4, "草稿");

    private final Integer code;
    private final String desc;

    OrderMasterStatusEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static OrderMasterStatusEnums getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (OrderMasterStatusEnums item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        OrderMasterStatusEnums enums = getByCode(code);
        return enums != null ? enums.getDesc() : "未知";
    }
}
