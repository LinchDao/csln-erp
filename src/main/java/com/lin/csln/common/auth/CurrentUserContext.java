package com.lin.csln.common.auth;

import org.springframework.util.StringUtils;

/**
 * 当前请求用户上下文
 */
public final class CurrentUserContext {

    private static final ThreadLocal<CurrentUser> CONTEXT = new ThreadLocal<>();

    private CurrentUserContext() {
    }

    public static void set(CurrentUser currentUser) {
        CONTEXT.set(currentUser);
    }

    public static CurrentUser get() {
        return CONTEXT.get();
    }

    public static String getUserId() {
        CurrentUser currentUser = get();
        return currentUser == null ? null : currentUser.getUserId();
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public static final class CurrentUser {
        private final String userId;
        private final String sessionId;
        private final String tokenJti;

        public CurrentUser(String userId, String sessionId, String tokenJti) {
            this.userId = userId;
            this.sessionId = sessionId;
            this.tokenJti = tokenJti;
        }

        public String getUserId() {
            return userId;
        }

        public String getSessionId() {
            return sessionId;
        }

        public String getTokenJti() {
            return tokenJti;
        }

        public boolean isValid() {
            return StringUtils.hasText(userId) && StringUtils.hasText(sessionId) && StringUtils.hasText(tokenJti);
        }
    }
}
