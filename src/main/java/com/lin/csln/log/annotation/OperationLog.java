package com.lin.csln.log.annotation;

import com.lin.csln.enums.BizIdSourceEnum;
import com.lin.csln.enums.OperationLogActionEnum;
import com.lin.csln.enums.OperationLogModuleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {

    OperationLogModuleEnum module();

    OperationLogActionEnum actionType();

    /**
     * 业务主键来源
     */
    BizIdSourceEnum bizIdSource();

    /**
     * 业务主键字段名
     */
    String bizIdField();

    /**
     * 是否记录前后差异
     */
    boolean recordDiff() default true;
}
