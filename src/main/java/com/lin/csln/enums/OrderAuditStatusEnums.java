package com.lin.csln.enums;

import lombok.Getter;

/**
 * 订单审核状态枚举
 */
@Getter
public enum OrderAuditStatusEnums {

    NO(0, "无需审核"),
    WAIT_AUDIT(1, "待审核"),
    AUDITED(2, "已审核"),
    REJECTED(3, "未通过");

    private final Integer code;
    private final String desc;

    OrderAuditStatusEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static OrderAuditStatusEnums getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (OrderAuditStatusEnums item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        OrderAuditStatusEnums enums = getByCode(code);
        return enums != null ? enums.getDesc() : "未知";
    }
}
