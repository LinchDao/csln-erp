package com.lin.csln.config;

import com.lin.csln.enums.FileCategoryEnum;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


/**
 * @Description: 文件上传配置类
 * @Author: linch
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadConfig {

    private String basePath;
    private String tempPath;
    private Long maxSize;
    private boolean allowOverride;
    private Map<String, String> supportTypes;
    private String accessPrefix;

    @PostConstruct
    public void initFileCategoryEnum() {
        // 加载配置中的扩展名到枚举（若枚举/配置未定义可忽略此逻辑）
        if (supportTypes != null && !supportTypes.isEmpty()) {
            FileCategoryEnum.loadSupportExtensions(supportTypes);
        }
    }
}
