package com.lin.csln.common.constants;


/**
 * @Description: '
 * @Author: linch
 */

/**
 * 通用状态码枚举
 */
public enum ResultCode {
    // 通用状态码
    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未登录或Token过期"),
    FORBIDDEN(403, "没有权限访问"),
    NOT_FOUND(404, "资源不存在"),
    SERVER_ERROR(500, "服务器内部错误"),

    // 业务自定义状态码（可根据项目扩展）
    USER_NOT_EXIST(10001, "用户不存在"),
    PASSWORD_ERROR(10002, "密码错误"),
    USER_DISABLED(10003, "账号已禁用");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
