package com.lin.csln.enums;

import lombok.Getter;

/**
 * 操作日志模块枚举
 */
@Getter
public enum OperationLogModuleEnum {
    PRODUCT_V2("product_v2", "商品"),
    PRODUCT("product", "商品"),
    USER("user", "用户"),
    CUSTOMER("customer", "客户"),
    DICT("dict", "字典"),
    ROLE("role", "角色"),
    ORDER_MASTER("order-master", "订单主单"),
    ORDER_SUB("order-sub", "订单子单"),
    PURCHASE_ORDER("purchase-order", "采购单"),
    PURCHASE_IN("purchase-in", "入库单"),
    AUTH("auth", "鉴权");

    private final String code;
    private final String desc;

    OperationLogModuleEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
