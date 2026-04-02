package com.lin.csln.enums;

import lombok.Getter;

/**
 * 主单状态枚举
 */
@Getter
public enum OrderMasterStatusEnums {

    WAIT_AUDIT(0, "待审核"),
    WAREHOUSE_PREPARING(1, "仓库准备中"),
    PART_SHIPPED(2, "部分已发货"),
    FINISHED(3, "订单已完成");

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
