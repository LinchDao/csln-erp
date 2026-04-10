package com.lin.csln.service;

import com.lin.csln.common.auth.AuthSessionInfo;

public interface AuthRedisService {

    void saveSession(AuthSessionInfo sessionInfo, long ttlMillis);

    AuthSessionInfo getSession(String sessionId);

    void deleteSession(String sessionId);

    void deleteUserSessions(String userId);

    boolean isBlacklisted(String jti);

    void blacklistToken(String jti, long ttlMillis);
}
