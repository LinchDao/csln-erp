package com.lin.csln.common.cache;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class CacheClient {

    private static final long DEFAULT_LOCK_EXPIRE_MILLIS = 3000L;
    private static final int DEFAULT_WAIT_RETRY_TIMES = 30;
    private static final long DEFAULT_WAIT_INTERVAL_MILLIS = 50L;
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT = createUnlockScript();

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public String getString(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof String text) {
            return text;
        }
        return JSON.toJSONString(value);
    }

    public void setString(String key, String value, long ttl, TimeUnit unit) {
        if (!StringUtils.hasText(value)) {
            return;
        }
        if (ttl > 0) {
            redisTemplate.opsForValue().set(key, value, ttl, unit);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    public <T> T getJson(String key, Class<T> clazz) {
        return parseValue(getString(key), json -> JSON.parseObject(json, clazz));
    }

    public <T> List<T> getJsonList(String key, Class<T> clazz) {
        return parseValue(getString(key), json -> JSON.parseArray(json, clazz));
    }

    public <T> T getOrLoadObjectWithMutex(String cacheKey,
                                          Class<T> clazz,
                                          Supplier<T> dbLoader,
                                          long ttl,
                                          TimeUnit unit,
                                          String lockPrefix) {
        return getOrLoadWithMutex(
                cacheKey,
                dbLoader,
                json -> JSON.parseObject(json, clazz),
                JSON::toJSONString,
                ttl,
                unit,
                lockPrefix
        );
    }

    public <T> List<T> getOrLoadListWithMutex(String cacheKey,
                                               Class<T> clazz,
                                               Supplier<List<T>> dbLoader,
                                               long ttl,
                                               TimeUnit unit,
                                               String lockPrefix) {
        return getOrLoadWithMutex(
                cacheKey,
                dbLoader,
                json -> JSON.parseArray(json, clazz),
                JSON::toJSONString,
                ttl,
                unit,
                lockPrefix
        );
    }

    private <T> T getOrLoadWithMutex(String cacheKey,
                                     Supplier<T> dbLoader,
                                     Function<String, T> parser,
                                     Function<T, String> serializer,
                                     long ttl,
                                     TimeUnit unit,
                                     String lockPrefix) {
        T cachedValue = parseValue(getString(cacheKey), parser);
        if (cachedValue != null) {
            return cachedValue;
        }

        String lockKey = StringUtils.hasText(lockPrefix)
                ? lockPrefix + cacheKey
                : "lock:" + cacheKey;
        String lockValue = UUID.randomUUID().toString();

        if (tryLock(lockKey, lockValue)) {
            try {
                T doubleCheckValue = parseValue(getString(cacheKey), parser);
                if (doubleCheckValue != null) {
                    return doubleCheckValue;
                }

                T dbValue = dbLoader.get();
                if (dbValue != null) {
                    setString(cacheKey, serializer.apply(dbValue), ttl, unit);
                }
                return dbValue;
            } finally {
                unlock(lockKey, lockValue);
            }
        }

        for (int i = 0; i < DEFAULT_WAIT_RETRY_TIMES; i++) {
            sleepQuietly(DEFAULT_WAIT_INTERVAL_MILLIS);
            T waitValue = parseValue(getString(cacheKey), parser);
            if (waitValue != null) {
                return waitValue;
            }
        }

        return dbLoader.get();
    }

    private boolean tryLock(String lockKey, String lockValue) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(
                lockKey, lockValue, DEFAULT_LOCK_EXPIRE_MILLIS, TimeUnit.MILLISECONDS
        );
        return Boolean.TRUE.equals(success);
    }

    private void unlock(String lockKey, String lockValue) {
        redisTemplate.execute(UNLOCK_SCRIPT, Collections.singletonList(lockKey), lockValue);
    }

    private void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private <T> T parseValue(String raw, Function<String, T> parser) {
        if (!StringUtils.hasText(raw)) {
            return null;
        }
        try {
            return parser.apply(raw);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static DefaultRedisScript<Long> createUnlockScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setResultType(Long.class);
        script.setScriptText("""
                if redis.call('get', KEYS[1]) == ARGV[1] then
                    return redis.call('del', KEYS[1])
                end
                return 0
                """);
        return script;
    }
}
