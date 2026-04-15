package com.lin.csln.config;


import com.lin.csln.common.cache.UserCache;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Description:
 * @Author: linch
 */

@Configuration
public class BeanConfig {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @EventListener(ContextRefreshedEvent.class)
    public void initUserCache() {
        UserCache.setRedisTemplate(redisTemplate);
    }
}