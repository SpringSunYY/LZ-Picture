package com.lz.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

/**
 * 参数工具类
 *
 * @author YY
 */
@Slf4j
public class ParamUtils {

    public static final String BEGIN_CREATE_TIME = "beginCreateTime";
    public static final String END_CREATE_TIME = "endCreateTime";

    /**
     * 通用反射获取 params 字段中指定 key 的字符串值
     *
     * @param queryObj 任意带有 getParams() 方法的对象
     * @param key      参数 key
     * @return 非空字符串或 null
     */
    public static String getSafeString(Object queryObj, String key) {
        Map<String, Object> params = getParamsMap(queryObj);
        return Optional.ofNullable(params)
                .map(m -> m.get(key))
                .map(Object::toString)
                .filter(StringUtils::isNotEmpty)
                .orElse(null);
    }

    /**
     * 获取 Integer 类型参数
     */
    public static Integer getSafeInteger(Object queryObj, String key) {
        try {
            Map<String, Object> params = getParamsMap(queryObj);
            return Optional.ofNullable(params)
                    .map(m -> m.get(key))
                    .map(Object::toString)
                    .filter(StringUtils::isNotEmpty)
                    .map(Integer::valueOf)
                    .orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 获取参数 Map，通过反射调用 getParams()
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> getParamsMap(Object queryObj) {
        if (queryObj == null) return null;
        try {
            Method method = queryObj.getClass().getMethod("getParams");
            Object result = method.invoke(queryObj);
            if (result instanceof Map<?, ?>) {
                return (Map<String, Object>) result;
            }
        } catch (Exception e) {
            log.error("getParamsMap error", e);
        }
        return null;
    }
}
