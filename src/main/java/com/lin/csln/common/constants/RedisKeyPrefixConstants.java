package com.lin.csln.common.constants;

public final class RedisKeyPrefixConstants {

    private RedisKeyPrefixConstants() {
    }

    public static final String USER_INFO = "user:info:";

    public static final String AUTH_SESSION = "auth:session:";
    public static final String AUTH_USER_SESSIONS = "auth:user:sessions:";
    public static final String AUTH_BLACKLIST = "auth:blacklist:";

    public static final String DICT_DETAIL = "dict:detail:";
    public static final String LOCK_DICT = "lock:dict:";

    public static final String STATISTICS = "statistics:";
    public static final String LOCK_STATISTICS = "lock:statistics:";
}
