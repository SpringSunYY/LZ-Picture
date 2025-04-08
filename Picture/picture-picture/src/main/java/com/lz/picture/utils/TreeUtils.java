package com.lz.picture.utils;

import com.lz.common.utils.StringUtils;

import java.util.function.Function;

/**
 * Project: Picture
 * Package: com.lz.picture.utils
 * Author: YY
 * CreateTime: 2025-04-07  22:32
 * Description: TreeUtils
 * Version: 1.0
 */
public class TreeUtils {
    /**
     * description: 获取所需父级节点祖级列表
     * author: YY
     * method: getAncestors
     * date: 2025/4/7 22:37
     * param:
     * param: parentId 父节点ID
     * param: ancestors 祖级列表 可以为空，为空拼接所有父级，不为空拼接自身+所有父级
     * param: queryFunction 查询方法
     * param: fieldGetter get方法，需要拼接的参数
     * param: parentIdGetter  父ID获取方法
     * param: startStr 起始字符串
     * param: separationStr 分隔符
     * return: java.lang.StringBuilder
     **/
    public static <T> StringBuilder getAncestors(String parentId, StringBuilder ancestors, ParentQueryFunction<T> queryFunction, Function<T, String> fieldGetter, Function<T, String> parentIdGetter, String startStr, String separationStr) {
        //获取父对象
        T parentObj = queryFunction.apply(parentId);
        //如果对象不为空
        if (StringUtils.isNotNull(parentObj)) {
            //获取当前ID
            String filed = fieldGetter.apply(parentObj);
            //判断是否为空 空不拼接分隔符
            if (ancestors.isEmpty()) {
                ancestors.insert(0, filed);
            } else {
                ancestors.insert(0, filed + separationStr);
            }
            return getAncestors(parentIdGetter.apply(parentObj), ancestors, queryFunction, fieldGetter, parentIdGetter, startStr, separationStr
            );
        } else {
            // 处理根节点情况
            if (ancestors.isEmpty() || !ancestors.toString().endsWith(startStr)) {
                ancestors.insert(0, startStr + separationStr);
            }
            return ancestors;
        }
    }
}
