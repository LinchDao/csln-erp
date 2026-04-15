package com.lin.csln.enums;

import lombok.Getter;

/**
 * 配送方式枚举
 */
@Getter
public enum DeliveryTypeEnums {

    EXPRESS(0, "快递"),
    INSTANT_FREIGHT(1, "即时货运"),
    SELF_PICKUP(3, "自提");

    private final Integer code;
    private final String desc;

    DeliveryTypeEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DeliveryTypeEnums getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (DeliveryTypeEnums item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        DeliveryTypeEnums enums = getByCode(code);
        return enums != null ? enums.getDesc() : "未知";
    }
}
