package com.lin.csln.log.resolver;

import com.lin.csln.common.dto.Result;
import com.lin.csln.enums.BizIdSourceEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerMapping;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 业务主键提取器
 */
@Component
public class BizIdResolver {

    public String resolve(BizIdSourceEnum bizIdSource, String bizIdField, Object[] args, Object result, HttpServletRequest request) {
        if (bizIdSource == null || !StringUtils.hasText(bizIdField)) {
            return null;
        }
        String field = bizIdField.trim();
        return switch (bizIdSource) {
            case PATH_VARIABLE -> findFromPathVariables(request, field);
            case REQUEST_PARAM -> findFromRequestParams(request, field);
            case REQUEST_BODY -> findFromArgs(args, field);
            case RESULT_DATA -> findFromResult(result, field);
        };
    }

    @SuppressWarnings("unchecked")
    private String findFromPathVariables(HttpServletRequest request, String fieldName) {
        if (request == null) {
            return null;
        }
        Object attr = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (!(attr instanceof Map)) {
            return null;
        }
        Map<String, String> pathMap = (Map<String, String>) attr;
        return findFromMap(pathMap, fieldName);
    }

    private String findFromRequestParams(HttpServletRequest request, String fieldName) {
        if (request == null) {
            return null;
        }
        Map<String, String[]> map = request.getParameterMap();
        if (map == null || map.isEmpty()) {
            return null;
        }
        String[] values = map.get(fieldName);
        if (values != null && values.length > 0 && StringUtils.hasText(values[0])) {
            return values[0].trim();
        }
        return null;
    }

    private String findFromArgs(Object[] args, String fieldName) {
        if (args == null || args.length == 0) {
            return null;
        }
        for (Object arg : args) {
            String value = findFromObject(arg, fieldName);
            if (StringUtils.hasText(value)) {
                return value;
            }
        }
        return null;
    }

    private String findFromResult(Object result, String fieldName) {
        if (result == null) {
            return null;
        }
        Object payload = result;
        if (result instanceof Result<?> response) {
            payload = response.getData();
        }
        return findFromObject(payload, fieldName);
    }

    private String findFromObject(Object obj, String fieldName) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Collection<?> collection) {
            if ("ids".equals(fieldName) || "idList".equals(fieldName)) {
                return collection.isEmpty() ? null : collection.toString();
            }
            return null;
        }
        if (obj.getClass().isArray()) {
            if ("ids".equals(fieldName) || "idList".equals(fieldName)) {
                return Arrays.toString((Object[]) obj);
            }
            return null;
        }
        if (obj instanceof String str) {
            return StringUtils.hasText(str) ? str.trim() : null;
        }
        if (obj instanceof Number number) {
            return String.valueOf(number);
        }
        if (obj instanceof Map<?, ?> map) {
            return findFromMap(map, fieldName);
        }
        return findFromBean(obj, fieldName);
    }

    private String findFromMap(Map<?, ?> map, String fieldName) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        return toText(map.get(fieldName));
    }

    private String findFromBean(Object obj, String fieldName) {
        List<Field> fields = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }

        return readFieldValue(fields, obj, fieldName);
    }

    private String readFieldValue(List<Field> fields, Object obj, String fieldName) {
        for (Field field : fields) {
            if (!fieldName.equals(field.getName())) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                return toText(value);
            } catch (IllegalAccessException ignored) {
                return null;
            }
        }
        return null;
    }

    private String toText(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String str) {
            return StringUtils.hasText(str) ? str.trim() : null;
        }
        if (value instanceof Number number) {
            return String.valueOf(number);
        }
        return null;
    }
}
