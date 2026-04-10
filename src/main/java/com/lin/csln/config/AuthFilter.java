package com.lin.csln.config;

import com.alibaba.fastjson2.JSON;
import com.lin.csln.common.auth.AuthSessionInfo;
import com.lin.csln.common.auth.CurrentUserContext;
import com.lin.csln.common.constants.ResultCode;
import com.lin.csln.common.dto.Result;
import com.lin.csln.service.AuthRedisService;
import com.lin.csln.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AuthFilter extends OncePerRequestFilter {
    private static final String JSON_UTF8 = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8";

    private static final List<String> WHITE_LIST = List.of(
            "/login",
            "/auth/refresh",
            "/error",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/doc.html",
            "/webjars/**"
    );

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private AuthRedisService authRedisService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (StringUtils.hasText(contextPath) && path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }
        for (String whitePath : WHITE_LIST) {
            if (PATH_MATCHER.match(whitePath, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtTokenUtil.resolveToken(request);
        if (!StringUtils.hasText(token)) {
            writeUnauthorizedResult(response);
            return;
        }

        Claims claims = jwtTokenUtil.parseClaimsSafely(token);
        if (claims == null) {
            writeUnauthorizedResult(response);
            return;
        }

        String tokenType = JwtTokenUtil.getTokenType(claims);
        if (!JwtTokenUtil.TOKEN_TYPE_ACCESS.equals(tokenType)) {
            writeUnauthorizedResult(response);
            return;
        }

        String userId = JwtTokenUtil.getUserId(claims);
        String sessionId = JwtTokenUtil.getSessionId(claims);
        String jti = JwtTokenUtil.getJti(claims);
        if (!StringUtils.hasText(userId) || !StringUtils.hasText(sessionId) || !StringUtils.hasText(jti)) {
            writeUnauthorizedResult(response);
            return;
        }
        if (authRedisService.isBlacklisted(jti)) {
            writeUnauthorizedResult(response);
            return;
        }
        AuthSessionInfo sessionInfo = authRedisService.getSession(sessionId);
        if (sessionInfo == null || !userId.equals(sessionInfo.getUserId())) {
            writeUnauthorizedResult(response);
            return;
        }

        try {
            CurrentUserContext.set(new CurrentUserContext.CurrentUser(userId, sessionId, jti));
            filterChain.doFilter(request, response);
        } finally {
            CurrentUserContext.clear();
        }
    }

    private void writeUnauthorizedResult(HttpServletResponse response) throws IOException {
        if (response.isCommitted()) {
            return;
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(JSON_UTF8);
        response.getWriter().write(JSON.toJSONString(Result.fail(ResultCode.UNAUTHORIZED)));
    }
}
