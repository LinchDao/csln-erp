package com.lin.csln.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.security")
public class AppSecurityProperties {

    private String passwordSalt = "defaultSalt123";

    private Boolean saltEnabled = true;

    private String defaultPassword = "123456";
}