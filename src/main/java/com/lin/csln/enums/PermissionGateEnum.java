package com.lin.csln.enums;

import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 门禁权限码枚举
 */
@Getter
public enum PermissionGateEnum {

    USER_CREATE("user:create", "user", "create", "用户-创建"),
    USER_UPDATE("user:update", "user", "update", "用户-修改"),
    USER_VIEW("user:view", "user", "view", "用户-查看"),
    USER_DELETE("user:delete", "user", "delete", "用户-删除"),
    USER_LIST("user:list", "user", "list", "用户-列表"),
    USER_EXPORT("user:export", "user", "export", "用户-导出"),
    USER_AUDIT("user:audit", "user", "audit", "用户-审核"),

    ROLE_CREATE("role:create", "role", "create", "角色-创建"),
    ROLE_UPDATE("role:update", "role", "update", "角色-修改"),
    ROLE_VIEW("role:view", "role", "view", "角色-查看"),
    ROLE_DELETE("role:delete", "role", "delete", "角色-删除"),
    ROLE_LIST("role:list", "role", "list", "角色-列表"),
    ROLE_EXPORT("role:export", "role", "export", "角色-导出"),
    ROLE_AUDIT("role:audit", "role", "audit", "角色-审核"),

    DICT_CREATE("dict:create", "dict", "create", "字典-创建"),
    DICT_UPDATE("dict:update", "dict", "update", "字典-修改"),
    DICT_VIEW("dict:view", "dict", "view", "字典-查看"),
    DICT_DELETE("dict:delete", "dict", "delete", "字典-删除"),
    DICT_LIST("dict:list", "dict", "list", "字典-列表"),
    DICT_EXPORT("dict:export", "dict", "export", "字典-导出"),
    DICT_AUDIT("dict:audit", "dict", "audit", "字典-审核"),

    MENU_UPDATE("menu:update", "menu", "update", "菜单-修改"),

    PRODUCT_CREATE("product:create", "product", "create", "商品-创建"),
    PRODUCT_UPDATE("product:update", "product", "update", "商品-修改"),
    PRODUCT_VIEW("product:view", "product", "view", "商品-查看"),
    PRODUCT_DELETE("product:delete", "product", "delete", "商品-删除"),
    PRODUCT_LIST("product:list", "product", "list", "商品-列表"),
    PRODUCT_EXPORT("product:export", "product", "export", "商品-导出"),
    PRODUCT_AUDIT("product:audit", "product", "audit", "商品-审核"),

    CUSTOMER_CREATE("customer:create", "customer", "create", "客户-创建"),
    CUSTOMER_UPDATE("customer:update", "customer", "update", "客户-修改"),
    CUSTOMER_VIEW("customer:view", "customer", "view", "客户-查看"),
    CUSTOMER_DELETE("customer:delete", "customer", "delete", "客户-删除"),
    CUSTOMER_LIST("customer:list", "customer", "list", "客户-列表"),
    CUSTOMER_EXPORT("customer:export", "customer", "export", "客户-导出"),
    CUSTOMER_AUDIT("customer:audit", "customer", "audit", "客户-审核"),

    SHOP_CREATE("shop:create", "shop", "create", "门店-创建"),
    SHOP_UPDATE("shop:update", "shop", "update", "门店-修改"),
    SHOP_VIEW("shop:view", "shop", "view", "门店-查看"),
    SHOP_DELETE("shop:delete", "shop", "delete", "门店-删除"),
    SHOP_LIST("shop:list", "shop", "list", "门店-列表"),
    SHOP_EXPORT("shop:export", "shop", "export", "门店-导出"),
    SHOP_AUDIT("shop:audit", "shop", "audit", "门店-审核"),

    WAREHOUSE_CREATE("warehouse:create", "warehouse", "create", "仓库-创建"),
    WAREHOUSE_UPDATE("warehouse:update", "warehouse", "update", "仓库-修改"),
    WAREHOUSE_VIEW("warehouse:view", "warehouse", "view", "仓库-查看"),
    WAREHOUSE_DELETE("warehouse:delete", "warehouse", "delete", "仓库-删除"),
    WAREHOUSE_LIST("warehouse:list", "warehouse", "list", "仓库-列表"),
    WAREHOUSE_EXPORT("warehouse:export", "warehouse", "export", "仓库-导出"),
    WAREHOUSE_AUDIT("warehouse:audit", "warehouse", "audit", "仓库-审核"),

    ORDER_MASTER_CREATE("order-master:create", "order-master", "create", "订单主单-创建"),
    ORDER_MASTER_UPDATE("order-master:update", "order-master", "update", "订单主单-修改"),
    ORDER_MASTER_VIEW("order-master:view", "order-master", "view", "订单主单-查看"),
    ORDER_MASTER_DELETE("order-master:delete", "order-master", "delete", "订单主单-删除"),
    ORDER_MASTER_LIST("order-master:list", "order-master", "list", "订单主单-列表"),
    ORDER_MASTER_EXPORT("order-master:export", "order-master", "export", "订单主单-导出"),
    ORDER_MASTER_AUDIT("order-master:audit", "order-master", "audit", "订单主单-审核"),

    ORDER_SUB_CREATE("order-sub:create", "order-sub", "create", "订单子单-创建"),
    ORDER_SUB_UPDATE("order-sub:update", "order-sub", "update", "订单子单-修改"),
    ORDER_SUB_VIEW("order-sub:view", "order-sub", "view", "订单子单-查看"),
    ORDER_SUB_DELETE("order-sub:delete", "order-sub", "delete", "订单子单-删除"),
    ORDER_SUB_LIST("order-sub:list", "order-sub", "list", "订单子单-列表"),
    ORDER_SUB_EXPORT("order-sub:export", "order-sub", "export", "订单子单-导出"),
    ORDER_SUB_AUDIT("order-sub:audit", "order-sub", "audit", "订单子单-审核"),
    ORDER_SUB_ASSIGN("order-sub:assign", "order-sub", "assign", "订单子单-分配"),

    PURCHASE_ORDER_CREATE("purchase-order:create", "purchase-order", "create", "采购单-创建"),
    PURCHASE_ORDER_UPDATE("purchase-order:update", "purchase-order", "update", "采购单-修改"),
    PURCHASE_ORDER_VIEW("purchase-order:view", "purchase-order", "view", "采购单-查看"),
    PURCHASE_ORDER_DELETE("purchase-order:delete", "purchase-order", "delete", "采购单-删除"),
    PURCHASE_ORDER_LIST("purchase-order:list", "purchase-order", "list", "采购单-列表"),
    PURCHASE_ORDER_EXPORT("purchase-order:export", "purchase-order", "export", "采购单-导出"),
    PURCHASE_ORDER_AUDIT("purchase-order:audit", "purchase-order", "audit", "采购单-审核"),

    PURCHASE_IN_CREATE("purchase-in:create", "purchase-in", "create", "入库单-创建"),
    PURCHASE_IN_UPDATE("purchase-in:update", "purchase-in", "update", "入库单-修改"),
    PURCHASE_IN_VIEW("purchase-in:view", "purchase-in", "view", "入库单-查看"),
    PURCHASE_IN_DELETE("purchase-in:delete", "purchase-in", "delete", "入库单-删除"),
    PURCHASE_IN_LIST("purchase-in:list", "purchase-in", "list", "入库单-列表"),
    PURCHASE_IN_EXPORT("purchase-in:export", "purchase-in", "export", "入库单-导出"),
    PURCHASE_IN_AUDIT("purchase-in:audit", "purchase-in", "audit", "入库单-审核");

    private static final Set<String> ALL_CODES;
    private static final Map<String, Set<String>> CODES_BY_MODULE;
    private static final Set<String> ACTIONS;

    static {
        Set<String> allCodes = new LinkedHashSet<>();
        Map<String, Set<String>> moduleCodeMap = new LinkedHashMap<>();
        for (PermissionGateEnum item : values()) {
            allCodes.add(item.code);
            moduleCodeMap.computeIfAbsent(item.module, key -> new LinkedHashSet<>()).add(item.code);
        }
        ALL_CODES = Collections.unmodifiableSet(allCodes);

        Map<String, Set<String>> immutableModuleMap = new LinkedHashMap<>();
        moduleCodeMap.forEach((key, value) ->
                immutableModuleMap.put(key, Collections.unmodifiableSet(value)));
        CODES_BY_MODULE = Collections.unmodifiableMap(immutableModuleMap);

        Set<String> actions = new LinkedHashSet<>();
        actions.add("create");
        actions.add("update");
        actions.add("view");
        actions.add("delete");
        actions.add("list");
        actions.add("export");
        actions.add("audit");
        ACTIONS = Collections.unmodifiableSet(actions);
    }

    private final String code;
    private final String module;
    private final String action;
    private final String desc;

    PermissionGateEnum(String code, String module, String action, String desc) {
        this.code = code;
        this.module = module;
        this.action = action;
        this.desc = desc;
    }

    public static boolean contains(String code) {
        if (!StringUtils.hasText(code)) {
            return false;
        }
        return ALL_CODES.contains(code);
    }

    public static Set<String> codesByModule(String module) {
        if (!StringUtils.hasText(module)) {
            return Collections.emptySet();
        }
        return CODES_BY_MODULE.getOrDefault(module, Collections.emptySet());
    }

    public static Set<String> allCodes() {
        return ALL_CODES;
    }

    public static Set<String> actions() {
        return ACTIONS;
    }
}
