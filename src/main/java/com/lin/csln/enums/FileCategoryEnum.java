package com.lin.csln.enums;





import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 文件类枚举
 * @Author: linch
 */
@Getter
public enum FileCategoryEnum {
    /**
     * 图片文件
     */
    IMAGE(1, "image", "图片文件", new HashSet<>()),
    /**
     * 其他文件
     */
    OTHER(2, "other", "其他文件", new HashSet<>());

    /**
     * 数据库存储值（int类型）
     */
    private final Integer code;

    /**
     * 前端交互编码（兼容原字符串传参）
     */
    private final String bizCode;

    /**
     * 类型描述
     */
    private final String desc;

    /**
     * 支持的扩展名集合（从yaml配置加载）
     */
    private final Set<String> supportExtensions;

    FileCategoryEnum(Integer code, String bizCode, String desc, Set<String> supportExtensions) {
        this.code = code;
        this.bizCode = bizCode;
        this.desc = desc;
        this.supportExtensions = supportExtensions;
    }

    // ==================== 核心方法 ====================

    /**
     * 根据数据库存储的int值获取枚举
     */
    public static FileCategoryEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(enumItem -> enumItem.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据前端传入的bizCode（如image/other）获取枚举
     */
    public static FileCategoryEnum getByBizCode(String bizCode) {
        if (StringUtils.isBlank(bizCode)) {
            return null;
        }
        return Arrays.stream(values())
                .filter(enumItem -> enumItem.getBizCode().equals(bizCode.trim().toLowerCase()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 校验扩展名是否支持
     */
    public static boolean isExtensionSupported(Integer fileTypeCode, String extension) {
        FileCategoryEnum enumItem = getByCode(fileTypeCode);
        return isExtensionSupported(enumItem, extension);
    }

    /**
     * 重载：通过bizCode校验扩展名
     */
    public static boolean isExtensionSupported(String bizCode, String extension) {
        FileCategoryEnum enumItem = getByBizCode(bizCode);
        return isExtensionSupported(enumItem, extension);
    }

    /**
     * 内部通用校验逻辑
     */
    private static boolean isExtensionSupported(FileCategoryEnum enumItem, String extension) {
        if (enumItem == null || StringUtils.isBlank(extension)) {
            return false;
        }
        // 统一转小写，兼容前端传大写扩展名
        String lowerExtension = extension.trim().toLowerCase();
        return enumItem.getSupportExtensions().contains(lowerExtension);
    }

    /**
     * 从yaml配置加载支持的扩展名（项目启动时调用）
     * @param configMap key: bizCode（image/other），value: 逗号分隔的扩展名
     */
    public static void loadSupportExtensions(java.util.Map<String, String> configMap) {
        if (configMap == null || configMap.isEmpty()) {
            return;
        }
        for (FileCategoryEnum enumItem : values()) {
            String extensionsStr = configMap.get(enumItem.getBizCode());
            if (StringUtils.isNotBlank(extensionsStr)) {
                Set<String> extensions = Arrays.stream(extensionsStr.split(","))
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet());
                enumItem.getSupportExtensions().clear();
                enumItem.getSupportExtensions().addAll(extensions);
            }
        }
    }

    /**
     * 获取支持的扩展名描述（用于错误提示）
     */
    public String getSupportExtensionsDesc() {
        return String.join("、", this.supportExtensions);
    }

    /**
     * 获取扩展名不支持的错误提示
     */
    public static String getUnsupportedExtensionMsg(Integer fileTypeCode, String extension) {
        FileCategoryEnum enumItem = getByCode(fileTypeCode);
        if (enumItem == null) {
            return "不支持的文件类型编码：" + fileTypeCode + "，支持的类型：" +
                    Arrays.stream(values()).map(item -> item.getCode() + "(" + item.getDesc() + ")").collect(Collectors.joining("、"));
        }
        return String.format("不支持的文件扩展名：%s，当前【%s】支持的扩展名：%s",
                extension, enumItem.getDesc(), enumItem.getSupportExtensionsDesc());
    }
}