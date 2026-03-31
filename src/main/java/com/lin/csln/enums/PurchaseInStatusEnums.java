package com.lin.csln.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description:
 * @Author: linch
 */

@Getter
@AllArgsConstructor
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

    /**
     * 根据code获取枚举
     */
    public static PurchaseInStatusEnums getByCode(Integer code) {
        for (PurchaseInStatusEnums enums : PurchaseInStatusEnums.values()) {
            if (enums.getCode().equals(code)) {
                return enums;
            }
        }
        return null;
    }
}
