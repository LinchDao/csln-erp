package com.lin.csln.common.auth;

import lombok.Data;

import java.io.Serializable;

/**
 * Redis 中存储的会话信息
 */
@Data
public class AuthSessionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sessionId;

    private String userId;

    private String refreshJti;

    private Boolean rememberMe;
}
