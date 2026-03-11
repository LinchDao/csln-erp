package com.lin.csln.common.cache;


import com.alibaba.fastjson2.JSON;
import com.lin.csln.common.dto.UserInfoDTO;
import com.lin.csln.utils.JwtTokenUtil;
import lombok.Setter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: linch
 */

public class UserCache {

    @Setter
    private static RedisTemplate<String, Object> redisTemplate;

    // 用户缓存key前缀
    private static final String USER_KEY_PREFIX = "user:info:";

    // 默认过期时间（秒）- 30分钟
    private static final long DEFAULT_EXPIRE = 1800;

    private UserCache() {
    }

    /**
     * 保存用户信息
     *
     * @param userInfo 用户信息
     */
    public static void saveUserInfo(UserInfoDTO userInfo) {
        if (userInfo == null) {
            return;
        }
        String key = USER_KEY_PREFIX + userInfo.getId();
        redisTemplate.opsForValue().set(key, JSON.toJSONString(userInfo), DEFAULT_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 保存用户信息并指定过期时间
     *
     * @param userId     用户ID
     * @param userInfo   用户信息
     * @param expireTime 过期时间（秒）
     */
    public static void saveUserInfo(Long userId, UserInfoDTO userInfo, long expireTime) {
        if (userId == null || userInfo == null) {
            return;
        }
        String key = USER_KEY_PREFIX + userId;
        if (expireTime > 0) {
            redisTemplate.opsForValue().set(key, JSON.toJSONString(userInfo), expireTime, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, JSON.toJSONString(userInfo));
        }
    }

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    public static UserInfoDTO getUserInfo(Long userId) {
        if (userId == null) {
            return null;
        }
        String key = USER_KEY_PREFIX + userId;
        String userinfoJson = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.hasLength(userinfoJson)) {
            return JSON.parseObject(userinfoJson, UserInfoDTO.class);
        }
        return null;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public static UserInfoDTO getUserInfo() {
        Long userId = JwtTokenUtil.getUserId();
        return getUserInfo(userId);
    }

    /**
     * 批量获取用户信息
     *
     * @param userIds 用户ID列表
     * @return 用户信息列表
     */
    public static List<UserInfoDTO> getUsersInfo(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> keys = userIds.stream()
                .map(id -> USER_KEY_PREFIX + id)
                .collect(Collectors.toList());

        List<Object> objects = redisTemplate.opsForValue().multiGet(keys);
        if (objects == null) {
            return new ArrayList<>();
        }

        return objects.stream()
                .filter(obj -> obj != null)
                .map(obj -> JSON.parseObject(JSON.toJSONString(obj), UserInfoDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * 删除用户信息
     *
     * @param userId 用户ID
     */
    public static void deleteUserInfo(String userId) {
        if (userId == null) {
            return;
        }
        String key = USER_KEY_PREFIX + userId;
        redisTemplate.delete(key);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 用户ID列表
     */
    public static void deleteUsersInfo(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return;
        }

        List<String> keys = userIds.stream()
                .map(id -> USER_KEY_PREFIX + id)
                .collect(Collectors.toList());

        redisTemplate.delete(keys);
    }

    /**
     * 更新用户信息
     *
     * @param userId   用户ID
     * @param userInfo 用户信息
     */
    public static void updateUserInfo(String userId, UserInfoDTO userInfo) {
        if (userId == null || userInfo == null) {
            return;
        }

        // 先获取原有信息，保留原有过期时间
        String key = USER_KEY_PREFIX + userId;
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);

        if (expire != null && expire > 0) {
            redisTemplate.opsForValue().set(key, JSON.toJSONString(userInfo), expire, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, JSON.toJSONString(userInfo), DEFAULT_EXPIRE, TimeUnit.SECONDS);
        }
    }

    /**
     * 判断用户信息是否存在
     *
     * @param userId 用户ID
     * @return true:存在 false:不存在
     */
    public static boolean hasUserInfo(String userId) {
        if (userId == null) {
            return false;
        }
        String key = USER_KEY_PREFIX + userId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 获取用户信息的过期时间
     *
     * @param userId 用户ID
     * @return 过期时间（秒），-1表示永不过期，-2表示key不存在
     */
    public static long getExpireTime(String userId) {
        if (userId == null) {
            return -2;
        }
        String key = USER_KEY_PREFIX + userId;
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 更新用户信息过期时间
     *
     * @param userId     用户ID
     * @param expireTime 新的过期时间（秒）
     * @return true:成功 false:失败
     */
    public static boolean updateExpireTime(String userId, long expireTime) {
        if (userId == null || expireTime <= 0) {
            return false;
        }
        String key = USER_KEY_PREFIX + userId;
        return Boolean.TRUE.equals(redisTemplate.expire(key, expireTime, TimeUnit.SECONDS));
    }


    /**
     * 从key中提取用户ID
     */
    private static String extractUserId(String key) {
        return key.replace(USER_KEY_PREFIX, "");
    }

    /**
     * 保存多个用户信息
     *
     * @param userMap 用户ID与用户信息的映射
     */
    public static void saveUsersInfo(java.util.Map<String, UserInfoDTO> userMap) {
        if (userMap == null || userMap.isEmpty()) {
            return;
        }

        userMap.forEach((userId, userInfo) -> {
            String key = USER_KEY_PREFIX + userId;
            redisTemplate.opsForValue().set(key, JSON.toJSONString(userInfo), DEFAULT_EXPIRE, TimeUnit.SECONDS);
        });
    }

    /**
     * 清空所有用户缓存
     */
    public static void clearAllUserCache() {
        Set<String> keys = redisTemplate.keys(USER_KEY_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}