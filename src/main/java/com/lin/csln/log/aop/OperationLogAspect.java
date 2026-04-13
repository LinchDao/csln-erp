package com.lin.csln.log.aop;

import com.alibaba.fastjson2.JSON;
import com.lin.csln.common.auth.CurrentUserContext;
import com.lin.csln.common.constants.ResultCode;
import com.lin.csln.common.dto.Result;
import com.lin.csln.log.annotation.OperationLog;
import com.lin.csln.log.publisher.OperationLogPublisher;
import com.lin.csln.log.resolver.BizIdResolver;
import com.lin.csln.log.snapshot.OperationSnapshotProvider;
import com.lin.csln.entity.LogDO;
import com.lin.csln.enums.OperationLogActionEnum;
import com.lin.csln.utils.JwtTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

/**
 * 操作日志切面
 */
@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    @Resource
    private BizIdResolver bizIdResolver;
    @Resource
    private OperationLogPublisher operationLogPublisher;
    @Resource
    private List<OperationSnapshotProvider> snapshotProviders;

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = getCurrentRequest();

        String bizId = bizIdResolver.resolve(operationLog.bizIdSource(), operationLog.bizIdField(), args, null, request);
        Object beforeSnapshot = null;
        if (operationLog.recordDiff() && needBeforeSnapshot(operationLog.actionType()) && StringUtils.hasText(bizId)) {
            beforeSnapshot = getSnapshot(operationLog, bizId);
        }

        Object result = null;
        Throwable throwable = null;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable ex) {
            throwable = ex;
            throw ex;
        } finally {
            try {
                if (!StringUtils.hasText(bizId)) {
                    bizId = bizIdResolver.resolve(operationLog.bizIdSource(), operationLog.bizIdField(), args, result, request);
                }

                Object afterSnapshot = null;
                if (operationLog.recordDiff() && needAfterSnapshot(operationLog.actionType()) && StringUtils.hasText(bizId)) {
                    afterSnapshot = getSnapshot(operationLog, bizId);
                }

                LogDO logDO = buildLogDO(operationLog, request, bizId, beforeSnapshot, afterSnapshot,
                        result, throwable, System.currentTimeMillis() - startTime);
                boolean requireNewTx = logDO.getStatus() != null && logDO.getStatus() == 0;
                operationLogPublisher.publish(logDO, requireNewTx);
            } catch (Exception logEx) {
                log.error("写入操作日志失败", logEx);
            }
        }
    }

    private LogDO buildLogDO(OperationLog operationLog,
                             HttpServletRequest request,
                             String bizId,
                             Object beforeSnapshot,
                             Object afterSnapshot,
                             Object result,
                             Throwable throwable,
                             long costTime) {
        LogDO logDO = new LogDO();
        logDO.setBizId(bizId);
        logDO.setModule(operationLog.module().getCode());
        logDO.setActionType(operationLog.actionType().getCode());
        logDO.setUserId(resolveUserId());
        if (request != null) {
            logDO.setRequestUrl(request.getRequestURI());
            logDO.setRequestMethod(request.getMethod());
        }
        logDO.setCostTime((int) Math.min(costTime, Integer.MAX_VALUE));
        logDO.setDiffData(buildDiffData(operationLog.actionType(), beforeSnapshot, afterSnapshot));

        if (throwable != null) {
            logDO.setStatus(0);
            logDO.setErrorMsg(limitLength(throwable.getMessage(), 1000));
            return logDO;
        }

        if (result instanceof Result<?> response && response.getCode() != ResultCode.SUCCESS.getCode()) {
            logDO.setStatus(0);
            logDO.setErrorMsg(limitLength(response.getMessage(), 1000));
            return logDO;
        }

        logDO.setStatus(1);
        return logDO;
    }

    private String buildDiffData(OperationLogActionEnum action, Object beforeSnapshot, Object afterSnapshot) {
        Map<String, Object> diff = new LinkedHashMap<>();
        diff.put("action", action.name());

        if (OperationLogActionEnum.CREATE == action) {
            diff.put("after", toMap(afterSnapshot));
            return JSON.toJSONString(diff);
        }
        if (OperationLogActionEnum.DELETE == action) {
            diff.put("before", toMap(beforeSnapshot));
            return JSON.toJSONString(diff);
        }

        diff.put("changes", buildChanges(beforeSnapshot, afterSnapshot));
        return JSON.toJSONString(diff);
    }

    private Map<String, Object> buildChanges(Object beforeSnapshot, Object afterSnapshot) {
        Map<String, Object> beforeMap = toMap(beforeSnapshot);
        Map<String, Object> afterMap = toMap(afterSnapshot);
        Set<String> keys = new LinkedHashSet<>();
        keys.addAll(beforeMap.keySet());
        keys.addAll(afterMap.keySet());

        Map<String, Object> changes = new LinkedHashMap<>();
        for (String key : keys) {
            Object beforeValue = beforeMap.get(key);
            Object afterValue = afterMap.get(key);
            if (!Objects.equals(beforeValue, afterValue)) {
                Map<String, Object> changeItem = new LinkedHashMap<>();
                changeItem.put("before", beforeValue);
                changeItem.put("after", afterValue);
                changes.put(key, changeItem);
            }
        }
        return changes;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> toMap(Object snapshot) {
        if (snapshot == null) {
            return Collections.emptyMap();
        }
        if (snapshot instanceof Map<?, ?> sourceMap) {
            Map<String, Object> map = new LinkedHashMap<>();
            sourceMap.forEach((k, v) -> map.put(String.valueOf(k), v));
            return map;
        }
        try {
            return JSON.parseObject(JSON.toJSONString(snapshot), LinkedHashMap.class);
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    private Object getSnapshot(OperationLog operationLog, String bizId) {
        for (OperationSnapshotProvider provider : snapshotProviders) {
            if (provider.supports(operationLog.module())) {
                return provider.snapshot(bizId);
            }
        }
        return null;
    }

    private boolean needBeforeSnapshot(OperationLogActionEnum action) {
        return OperationLogActionEnum.CREATE != action;
    }

    private boolean needAfterSnapshot(OperationLogActionEnum action) {
        return OperationLogActionEnum.DELETE != action;
    }

    private String resolveUserId() {
        String userId = CurrentUserContext.getUserId();
        if (StringUtils.hasText(userId)) {
            return userId;
        }
        return JwtTokenUtil.getUserId();
    }

    private HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        }
        return null;
    }

    private String limitLength(String value, int maxLength) {
        if (!StringUtils.hasText(value)) {
            return value;
        }
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
