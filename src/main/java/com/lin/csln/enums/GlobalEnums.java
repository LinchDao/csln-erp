package com.lin.csln.enums;


/**
 * @Description:
 * @Author: linch
 */

/**
 * 全局通用枚举类（包含0/1状态标识）
 * 适用于：是否删除、是否启用、是否默认、是否成功等二值场景
 */
public enum GlobalEnums {

    NO(0, "否"),
    YES(1, "是"),
    ;
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
        for (GlobalEnums enums : GlobalEnums.values()) {
            if (enums.getCode().equals(code)) {
                return enums;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        GlobalEnums enums = getByCode(code);
        return enums != null ? enums.getDesc() : "未知";
    }

    // ========== Getter方法 ==========
    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
