package com.lin.csln.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 采购入库审核状态枚举
 */
@Getter
public enum PurchaseInStatusEnums {

    WAIT_AUDIT(0, "待审核"),
    AUDITED(1, "已审核"),
    REJECTED(2, "未通过");

    /**
     * 存在数据库里的值
     */
    @EnumValue
    private final Integer code;

    /**
     * 前端显示的值
     */
    @JsonValue
    private final String desc;

    PurchaseInStatusEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static PurchaseInStatusEnums getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PurchaseInStatusEnums item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
