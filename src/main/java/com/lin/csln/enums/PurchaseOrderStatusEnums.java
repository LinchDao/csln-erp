package com.lin.csln.enums;

import lombok.Getter;

/**
 * 采购单状态枚举
 */
@Getter
public enum PurchaseOrderStatusEnums {

    WAIT_IN(0, "待入库"),
    PART_IN(1, "部分入库"),
    FINISHED(2, "已完成"),
    CANCELED(3, "取消");

    private final Integer code;
    private final String desc;

    PurchaseOrderStatusEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PurchaseOrderStatusEnums getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PurchaseOrderStatusEnums item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        PurchaseOrderStatusEnums enums = getByCode(code);
        return enums == null ? "" : enums.getDesc();
    }
}
