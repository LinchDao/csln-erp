package com.lin.csln.enums;

import lombok.Getter;

/**
 * 全局二值枚举（是/否）
 */
@Getter
public enum GlobalEnums {

    NO(0, "否"),
    YES(1, "是");

    private final Integer code;
    private final String desc;

    GlobalEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static GlobalEnums getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (GlobalEnums item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        GlobalEnums enums = getByCode(code);
        return enums != null ? enums.getDesc() : "未知";
    }
}
