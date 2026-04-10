package com.lin.csln.service.impl;

import com.lin.csln.common.auth.AuthSessionInfo;
import com.lin.csln.common.constants.ResultCode;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.config.AppSecurityProperties;
import com.lin.csln.dto.login.LoginReqDTO;
import com.lin.csln.dto.login.LoginRespDTO;
import com.lin.csln.entity.UserDO;
import com.lin.csln.service.AuthRedisService;
import com.lin.csln.service.AuthService;
import com.lin.csln.service.UserService;
import com.lin.csln.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserService userService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private AppSecurityProperties securityProperties;
    @Resource
    private AuthRedisService authRedisService;

    @Override
    public LoginRespDTO login(LoginReqDTO loginRequest) {
        String username = loginRequest.getUsername() == null ? null : loginRequest.getUsername().trim();
        UserDO user = userService.getUserByUsername(username);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST.getCode(), ResultCode.USER_NOT_EXIST.getMessage());
        }

        String encryptPwd = encryptPassword(loginRequest.getPassword());
        if (!encryptPwd.equals(user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR.getCode(), ResultCode.PASSWORD_ERROR.getMessage());
        }
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_DISABLED.getCode(), ResultCode.USER_DISABLED.getMessage());
        }

        boolean rememberMe = Boolean.TRUE.equals(loginRequest.getRememberMe());
        long accessExpireMillis = jwtTokenUtil.getNormalExpireTime();
        long refreshExpireMillis = rememberMe ? jwtTokenUtil.getRememberExpireTime() : jwtTokenUtil.getNormalExpireTime();
        String sessionId = newId();
        String accessJti = newId();
        String refreshJti = newId();

        String accessToken = jwtTokenUtil.generateAccessToken(user.getId(), sessionId, accessJti, accessExpireMillis);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getId(), sessionId, refreshJti, refreshExpireMillis, rememberMe);

        AuthSessionInfo sessionInfo = new AuthSessionInfo();
        sessionInfo.setSessionId(sessionId);
        sessionInfo.setUserId(user.getId());
        sessionInfo.setRefreshJti(refreshJti);
        sessionInfo.setRememberMe(rememberMe);
        authRedisService.saveSession(sessionInfo, refreshExpireMillis);

        return buildTokenResp(accessToken, refreshToken, accessExpireMillis);
    }

    @Override
    public LoginRespDTO refresh(String refreshToken) {
        Claims claims = jwtTokenUtil.parseClaimsSafely(refreshToken);
        if (claims == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "刷新令牌无效或已过期");
        }

        String tokenType = JwtTokenUtil.getTokenType(claims);
        if (!JwtTokenUtil.TOKEN_TYPE_REFRESH.equals(tokenType)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "非法刷新令牌");
        }

        String userId = JwtTokenUtil.getUserId(claims);
        String sessionId = JwtTokenUtil.getSessionId(claims);
        String refreshJti = JwtTokenUtil.getJti(claims);
        if (!StringUtils.hasText(userId) || !StringUtils.hasText(sessionId) || !StringUtils.hasText(refreshJti)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "刷新令牌缺少关键信息");
        }
        if (authRedisService.isBlacklisted(refreshJti)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "刷新令牌已失效");
        }

        AuthSessionInfo sessionInfo = authRedisService.getSession(sessionId);
        if (sessionInfo == null || !userId.equals(sessionInfo.getUserId())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "登录会话不存在或已失效");
        }
        if (!refreshJti.equals(sessionInfo.getRefreshJti())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "刷新令牌已失效");
        }

        boolean rememberMe = Boolean.TRUE.equals(sessionInfo.getRememberMe());
        long accessExpireMillis = jwtTokenUtil.getNormalExpireTime();
        long refreshExpireMillis = rememberMe ? jwtTokenUtil.getRememberExpireTime() : jwtTokenUtil.getNormalExpireTime();
        String newAccessJti = newId();
        String newRefreshJti = newId();

        String newAccessToken = jwtTokenUtil.generateAccessToken(userId, sessionId, newAccessJti, accessExpireMillis);
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(userId, sessionId, newRefreshJti, refreshExpireMillis, rememberMe);

        long oldRefreshRemainMs = jwtTokenUtil.getRemainingMillis(claims);
        if (oldRefreshRemainMs > 0) {
            authRedisService.blacklistToken(refreshJti, oldRefreshRemainMs);
        }

        sessionInfo.setRefreshJti(newRefreshJti);
        sessionInfo.setRememberMe(rememberMe);
        authRedisService.saveSession(sessionInfo, refreshExpireMillis);

        return buildTokenResp(newAccessToken, newRefreshToken, accessExpireMillis);
    }

    @Override
    public void logout(String accessToken) {
        Claims claims = jwtTokenUtil.parseClaimsSafely(accessToken);
        if (claims == null) {
            return;
        }
        String tokenType = JwtTokenUtil.getTokenType(claims);
        if (!JwtTokenUtil.TOKEN_TYPE_ACCESS.equals(tokenType)) {
            return;
        }

        String jti = JwtTokenUtil.getJti(claims);
        long remainMillis = jwtTokenUtil.getRemainingMillis(claims);
        if (remainMillis > 0 && StringUtils.hasText(jti)) {
            authRedisService.blacklistToken(jti, remainMillis);
        }

        String sessionId = JwtTokenUtil.getSessionId(claims);
        if (StringUtils.hasText(sessionId)) {
            authRedisService.deleteSession(sessionId);
        }
    }

    private LoginRespDTO buildTokenResp(String accessToken, String refreshToken, long accessExpireMillis) {
        LoginRespDTO resp = new LoginRespDTO();
        resp.setAccessToken(accessToken);
        resp.setRefreshToken(refreshToken);
        resp.setTokenType("Bearer");
        resp.setExpiresIn(accessExpireMillis / 1000);
        resp.setToken(accessToken);
        return resp;
    }

    private String encryptPassword(String plainPassword) {
        String salt = securityProperties.getPasswordSalt();
        boolean saltEnabled = Boolean.TRUE.equals(securityProperties.getSaltEnabled());
        if (saltEnabled) {
            return DigestUtils.md5Hex(plainPassword + salt);
        }
        return DigestUtils.md5Hex(plainPassword);
    }

    private String newId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
