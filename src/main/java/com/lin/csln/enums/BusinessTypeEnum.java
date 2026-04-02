package com.lin.csln.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 文件业务类型枚举
 */
@Getter
public enum BusinessTypeEnum {
    /**
     * 商品主图
     */
    PRODUCT_MAIN_IMAGE("product_main_image", "商品主图"),
    /**
     * 商品详情图
     */
    PRODUCT_DETAIL_IMAGE("product_detail_image", "商品详情图"),
    /**
     * 商品附件
     */
    PRODUCT_ATTACH("product_attach", "商品附件"),
    /**
     * 订单附件
     */
    ORDER_ATTACH("order_attach", "订单附件"),
    /**
     * 用户头像
     */
    USER_AVATAR("user_avatar", "用户头像"),
    /**
     * 其他业务（兜底）
     */
    OTHER("other", "其他业务");

    /**
     * 数据库存储值（字符串，语义化便于维护）
     */
    private final String code;

    /**
     * 业务描述
     */
    private final String desc;

    BusinessTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据数据库存储的code获取枚举
     */
    public static BusinessTypeEnum getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return Arrays.stream(values())
                .filter(enumItem -> enumItem.getCode().equals(code.trim()))
                .findFirst()
                .orElse(null);
    }
}
