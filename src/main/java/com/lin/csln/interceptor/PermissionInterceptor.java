package com.lin.csln.interceptor;

import com.lin.csln.common.auth.annotation.RequirePermission;
import com.lin.csln.common.cache.UserCache;
import com.lin.csln.common.constants.ResultCode;
import com.lin.csln.common.dto.UserInfoDTO;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.enums.PermissionGateEnum;
import com.lin.csln.service.UserService;
import com.lin.csln.utils.JwtTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        RequirePermission requirePermission = findRequirePermission(handlerMethod);
        if (requirePermission == null) {
            return true;
        }

        List<String> requiredPermissions = Arrays.stream(requirePermission.value())
                .map(PermissionGateEnum::getCode)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
        if (requiredPermissions.isEmpty()) {
            return true;
        }

        String userId = JwtTokenUtil.getUserId();
        if (!StringUtils.hasText(userId)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }

        UserInfoDTO userInfo = UserCache.getUserInfo(userId);
        if (userInfo == null) {
            userInfo = userService.getUserInfoById(userId);
            if (userInfo == null) {
                throw new BusinessException(ResultCode.UNAUTHORIZED);
            }
            UserCache.saveUserInfo(userInfo);
        }

        if (Boolean.TRUE.equals(userInfo.getIsAdmin())) {
            return true;
        }

        List<String> permissionList = userInfo.getPermissionList();
        if (permissionList == null || permissionList.isEmpty()) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        Set<String> userPermissions = permissionList.stream()
                .filter(StringUtils::hasText)
                .collect(Collectors.toSet());

        boolean hasPermission = requiredPermissions.stream().anyMatch(userPermissions::contains);
        if (!hasPermission) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        return true;
    }

    private RequirePermission findRequirePermission(HandlerMethod handlerMethod) {
        RequirePermission methodAnno = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getMethod(), RequirePermission.class);
        if (methodAnno != null) {
            return methodAnno;
        }
        return AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getBeanType(), RequirePermission.class);
    }
}
