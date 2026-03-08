package com.lin.csln.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description:应用安全配置属性类
 * @Author: linch
 */

@Data
@Component
@ConfigurationProperties(prefix = "app.security")
public class AppSecurityProperties {

    /**
     * 密码加密盐值
     */
    private String passwordSalt = "defaultSalt123";  // 设置默认值

    /**
     * 是否启用盐值
     */
    private Boolean saltEnabled = true;
}