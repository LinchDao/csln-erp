package com.lin.csln.service.impl;

import com.lin.csln.common.auth.AuthSessionInfo;
import com.lin.csln.common.constants.RedisKeyPrefixConstants;
import com.lin.csln.service.AuthRedisService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class AuthRedisServiceImpl implements AuthRedisService {

    private static final String SESSION_KEY_PREFIX = RedisKeyPrefixConstants.AUTH_SESSION;
    private static final String USER_SESSIONS_KEY_PREFIX = RedisKeyPrefixConstants.AUTH_USER_SESSIONS;
    private static final String BLACKLIST_KEY_PREFIX = RedisKeyPrefixConstants.AUTH_BLACKLIST;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveSession(AuthSessionInfo sessionInfo, long ttlMillis) {
        if (sessionInfo == null || !StringUtils.hasText(sessionInfo.getSessionId())
                || !StringUtils.hasText(sessionInfo.getUserId()) || ttlMillis <= 0) {
            return;
        }
        String sessionKey = SESSION_KEY_PREFIX + sessionInfo.getSessionId();
        redisTemplate.opsForValue().set(sessionKey, sessionInfo, ttlMillis, TimeUnit.MILLISECONDS);

        String userSessionsKey = USER_SESSIONS_KEY_PREFIX + sessionInfo.getUserId();
        redisTemplate.opsForSet().add(userSessionsKey, sessionInfo.getSessionId());
        redisTemplate.expire(userSessionsKey, ttlMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public AuthSessionInfo getSession(String sessionId) {
        if (!StringUtils.hasText(sessionId)) {
            return null;
        }
        Object value = redisTemplate.opsForValue().get(SESSION_KEY_PREFIX + sessionId);
        return toSessionInfo(value);
    }

    @Override
    public void deleteSession(String sessionId) {
        if (!StringUtils.hasText(sessionId)) {
            return;
        }
        AuthSessionInfo sessionInfo = getSession(sessionId);
        redisTemplate.delete(SESSION_KEY_PREFIX + sessionId);
        if (sessionInfo == null || !StringUtils.hasText(sessionInfo.getUserId())) {
            return;
        }
        redisTemplate.opsForSet().remove(USER_SESSIONS_KEY_PREFIX + sessionInfo.getUserId(), sessionId);
    }

    @Override
    public void deleteUserSessions(String userId) {
        if (!StringUtils.hasText(userId)) {
            return;
        }
        String userSessionsKey = USER_SESSIONS_KEY_PREFIX + userId;
        Set<Object> sessionIds = redisTemplate.opsForSet().members(userSessionsKey);
        if (sessionIds != null && !sessionIds.isEmpty()) {
            for (Object sessionIdObj : sessionIds) {
                if (sessionIdObj == null) {
                    continue;
                }
                String sessionId = String.valueOf(sessionIdObj);
                redisTemplate.delete(SESSION_KEY_PREFIX + sessionId);
            }
        }
        redisTemplate.delete(userSessionsKey);
    }

    @Override
    public boolean isBlacklisted(String jti) {
        if (!StringUtils.hasText(jti)) {
            return false;
        }
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_KEY_PREFIX + jti));
    }

    @Override
    public void blacklistToken(String jti, long ttlMillis) {
        if (!StringUtils.hasText(jti) || ttlMillis <= 0) {
            return;
        }
        redisTemplate.opsForValue().set(BLACKLIST_KEY_PREFIX + jti, "1", ttlMillis, TimeUnit.MILLISECONDS);
    }

    @SuppressWarnings("unchecked")
    private AuthSessionInfo toSessionInfo(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof AuthSessionInfo sessionInfo) {
            return sessionInfo;
        }
        if (value instanceof Map<?, ?> mapValue) {
            AuthSessionInfo sessionInfo = new AuthSessionInfo();
            Object sid = ((Map<String, Object>) mapValue).get("sessionId");
            Object uid = ((Map<String, Object>) mapValue).get("userId");
            Object refreshJti = ((Map<String, Object>) mapValue).get("refreshJti");
            Object rememberMe = ((Map<String, Object>) mapValue).get("rememberMe");
            sessionInfo.setSessionId(sid == null ? null : String.valueOf(sid));
            sessionInfo.setUserId(uid == null ? null : String.valueOf(uid));
            sessionInfo.setRefreshJti(refreshJti == null ? null : String.valueOf(refreshJti));
            if (rememberMe != null) {
                sessionInfo.setRememberMe(Boolean.valueOf(String.valueOf(rememberMe)));
            }
            return sessionInfo;
        }
        return null;
    }
}
