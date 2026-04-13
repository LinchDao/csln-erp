package com.lin.csln.enums;

import lombok.Getter;

/**
 * 操作日志动作枚举
 */
@Getter
public enum OperationLogActionEnum {

    CREATE("CREATE", "新增"),
    UPDATE("UPDATE", "修改"),
    DELETE("DELETE", "删除"),
    AUDIT("AUDIT", "审核"),
    EXPORT("EXPORT", "导出"),
    LOGIN("LOGIN", "登录"),
    LOGOUT("LOGOUT", "退出登录");

    private final String code;
    private final String desc;

    OperationLogActionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

