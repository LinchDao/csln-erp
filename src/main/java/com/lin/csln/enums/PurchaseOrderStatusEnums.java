package com.lin.csln.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description:
 * @Author: linch
 */

@Getter
@AllArgsConstructor
public enum PurchaseOrderStatusEnums {

    WAIT_IN(0, "待入库"),
    PART_IN(1, "部分入库"),
    FINISHED(2, "已完成"),
    CANCELED(3, "取消");

    private final Integer code;
    private final String desc;

    public static PurchaseOrderStatusEnums getByCode(Integer code) {
        for (PurchaseOrderStatusEnums enums : values()) {
            if (enums.getCode().equals(code)) {
                return enums;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        PurchaseOrderStatusEnums enums = getByCode(code);
        return enums == null ? "" : enums.getDesc();
    }
}
