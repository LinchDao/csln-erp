package com.lin.csln.common.exception;


/**
 * @Description:
 * @Author: linch
 */


import com.lin.csln.common.constants.ResultCode;

/**
 * 自定义业务异常
 */
public class BusinessException extends RuntimeException {

    private int code;

    public int getCode() {
        return code;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        int code = ResultCode.FAIL.getCode();
    }

    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
    }
}
