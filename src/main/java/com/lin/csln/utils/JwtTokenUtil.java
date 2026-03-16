package com.lin.csln.utils;


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

/**
 * @Description:
 * @Author: linch
 */

@Component
public class JwtTokenUtil {
    // 设置有效期30min
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

    /**
     * 生成Token
     *
     * @param userId     用户ID
     * @param rememberMe 是否记住我
     * @return JWT Token
     */
    public String generateToken(String userId, boolean rememberMe) {
        long expireTime = rememberMe ? rememberExpireTime : normalExpireTime;
        return Jwts.builder()
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(key)
                .compact();
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

    /**
     * 从Token中获取用户ID
     */
    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return String.valueOf(claims.getSubject());
    }

    private static String getTokenFromRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                // 使用配置的tokenKey获取token
                String token = request.getHeader(jwtTokenUtil.tokenKey);
                // 如果token以Bearer开头，去掉前缀
                if (token != null && token.startsWith("Bearer ")) {
                    return token.substring(7);
                }
                return token;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUserId() {
        String token = getTokenFromRequest();
        if (StringUtils.hasLength(token)) {
            return jwtTokenUtil.getUserIdFromToken(token);
        }
        return null;
    }

}