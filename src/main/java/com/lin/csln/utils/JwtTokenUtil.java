package com.lin.csln.utils;


import com.lin.csln.common.auth.CurrentUserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * @Description:
 * @Author: linch
 */

@Component
public class JwtTokenUtil {
    public static final String TOKEN_TYPE_ACCESS = "access";
    public static final String TOKEN_TYPE_REFRESH = "refresh";
    public static final String CLAIM_UID = "uid";
    public static final String CLAIM_SID = "sid";
    public static final String CLAIM_TYPE = "type";
    public static final String CLAIM_RM = "rm";

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expire.normal}")
    private long normalExpireTime;

    @Value("${jwt.expire.remember}")
    private long rememberExpireTime;

    private SecretKey key;

    @Value("${jwt.tokenKey}")
    private String tokenKey;

    @Setter
    private static JwtTokenUtil jwtTokenUtil;

    @PostConstruct
    public void init() {
        // 确保密钥长度足够，不足则补全
        String secret = secretKey.length() < 32 ? secretKey + "00000000000000000000000000000000" : secretKey;
        this.key = Keys.hmacShaKeyFor(secret.substring(0, 32).getBytes(StandardCharsets.UTF_8));
        JwtTokenUtil.setJwtTokenUtil(this);
    }

    public String generateAccessToken(String userId, String sessionId, String jti, long expireMillis) {
        return generateToken(userId, sessionId, jti, TOKEN_TYPE_ACCESS, expireMillis, null);
    }

    public String generateRefreshToken(String userId, String sessionId, String jti, long expireMillis, boolean rememberMe) {
        return generateToken(userId, sessionId, jti, TOKEN_TYPE_REFRESH, expireMillis, Map.of(CLAIM_RM, rememberMe));
    }

    private String generateToken(String userId, String sessionId, String jti, String tokenType,
                                 long expireMillis, Map<String, Object> extClaims) {
        var builder = Jwts.builder()
                .id(jti)
                .subject(userId)
                .claim(CLAIM_UID, userId)
                .claim(CLAIM_SID, sessionId)
                .claim(CLAIM_TYPE, tokenType)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expireMillis))
                .signWith(key);
        if (extClaims != null && !extClaims.isEmpty()) {
            builder.claims(extClaims);
        }
        return builder.compact();
    }

    /**
     * 验证Token有效性
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Claims parseClaimsSafely(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        try {
            return parseClaims(token);
        } catch (Exception ex) {
            return null;
        }
    }

    public long getRemainingMillis(Claims claims) {
        if (claims == null || claims.getExpiration() == null) {
            return 0;
        }
        return Math.max(claims.getExpiration().getTime() - System.currentTimeMillis(), 0);
    }

    /**
     * 从Token中获取用户ID
     */
    public String getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return getUserId(claims);
    }

    public String resolveToken(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String token = request.getHeader(tokenKey);
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

    private static String getTokenFromRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                if (jwtTokenUtil == null) {
                    return null;
                }
                return jwtTokenUtil.resolveToken(request);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static String getUserId() {
        String userId = CurrentUserContext.getUserId();
        if (StringUtils.hasText(userId)) {
            return userId;
        }
        String token = getTokenFromRequest();
        if (StringUtils.hasLength(token) && jwtTokenUtil != null) {
            Claims claims = jwtTokenUtil.parseClaimsSafely(token);
            return getUserId(claims);
        }
        return null;
    }

    public static String getUserId(Claims claims) {
        if (claims == null) {
            return null;
        }
        Object uid = claims.get(CLAIM_UID);
        if (uid != null) {
            return String.valueOf(uid);
        }
        return claims.getSubject();
    }

    public static String getSessionId(Claims claims) {
        if (claims == null) {
            return null;
        }
        Object sid = claims.get(CLAIM_SID);
        return sid == null ? null : String.valueOf(sid);
    }

    public static String getTokenType(Claims claims) {
        if (claims == null) {
            return null;
        }
        Object type = claims.get(CLAIM_TYPE);
        return type == null ? null : String.valueOf(type);
    }

    public static String getJti(Claims claims) {
        if (claims == null) {
            return null;
        }
        return claims.getId();
    }

    public long getNormalExpireTime() {
        return normalExpireTime;
    }

    public long getRememberExpireTime() {
        return rememberExpireTime;
    }

    public String getTokenKey() {
        return tokenKey;
    }
}
