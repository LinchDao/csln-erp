package com.lin.csln.config;

import com.lin.csln.filter.AuthFilter;
import jakarta.annotation.Resource;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class AuthFilterConfig {

    @Resource
    private AuthFilter authFilter;

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilterRegistrationBean() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(authFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        return registrationBean;
    }
}
