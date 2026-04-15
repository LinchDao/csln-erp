package com.lin.csln.enums;

import lombok.Getter;

/**
 * Role enums
 */
@Getter
public enum RoleEnums {

    SUPER_ADMIN("00000000000000000000000000000001", "SUPER_ADMIN", "Super Admin", "Highest system permission, manages all data"),
    BOSS("00000000000000000000000000000002", "BOSS", "Boss", "View full data, no edit/delete permission"),
    SHOP_MANAGER("00000000000000000000000000000003", "SHOP_MANAGER", "Shop Manager", "Manage daily operations of owned shops"),
    WAREHOUSE_MANAGER("00000000000000000000000000000004", "WAREHOUSE_MANAGER", "Warehouse Manager", "Manage inventory and operations of owned warehouses"),
    SHOP_CLERK("00000000000000000000000000000005", "SHOP_CLERK", "Shop Clerk", "Basic shop operations like order taking and cashier"),
    DISPATCHER("00000000000000000000000000000006", "DISPATCHER", "Dispatcher", "Warehouse picking and outbound operations");

    private final String id;
    private final String code;
    private final String name;
    private final String remark;

    RoleEnums(String id, String code, String name, String remark) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.remark = remark;
    }
}