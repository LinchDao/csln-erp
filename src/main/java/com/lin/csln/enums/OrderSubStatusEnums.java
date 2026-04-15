package com.lin.csln.enums;

import lombok.Getter;

/**
 * 子单状态枚举
 */
@Getter
public enum OrderSubStatusEnums {

    WAIT_AUDIT(0, "待审核"),
    WAIT_ALLOCATE(1, "待分配"),
    PICKING(2, "配货中"),
    PICKED(3, "配货完成"),
    SHIPPED(4, "已发货"),
    TRANSFERING(5, "调货中"),
    WAIT_REPLENISH(6, "待补货"),
    WAIT_RETURN_ARRIVE(7, "待退货到场");

    private final Integer code;
    private final String desc;

    OrderSubStatusEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static OrderSubStatusEnums getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (OrderSubStatusEnums item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        OrderSubStatusEnums enums = getByCode(code);
        return enums != null ? enums.getDesc() : "未知";
    }
}
