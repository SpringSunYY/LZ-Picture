package com.lz.config.model.enmus;


import com.lz.common.utils.StringUtils;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Project: config
 * Description: CFileLogOssTypeEnum
 * 日志文件oss类型
 * Version: 1.0
 */
@Getter
public enum CFileLogOssTypeEnum {

    OSS_TYPE_0("官方", "0"),
    OSS_TYPE_1("阿里云", "1"),
    OSS_TYPE_2("腾讯云", "2");

    // 获取 text
    private final String text; // 字典描述（label）
    // 获取 value
    private final String value; // 字典值

    private static final Map<String, CFileLogOssTypeEnum> VALUE_TO_ENUM = new ConcurrentHashMap<>();

    static {
        // 构建 value -> enum 的映射，以提高查找效率
        for (CFileLogOssTypeEnum enumValue : CFileLogOssTypeEnum.values()) {
            VALUE_TO_ENUM.put(enumValue.value, enumValue);
        }
    }

    // 构造函数
    CFileLogOssTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取所有枚举常量的值列表
     *
     * @return 所有枚举常量的值列表
     */
    public static List<String> getValues() {
        return Arrays.stream(values())  // 获取所有枚举常量
                .map(CFileLogOssTypeEnum::getValue)  // 提取每个枚举常量的 value
                .collect(Collectors.toList());  // 转换为 List
    }

    /**
     * 根据 value 获取对应的枚举
     *
     * @param value 枚举的值
     * @return 对应的枚举对象，如果没有找到则返回 Optional.empty()
     */
    public static Optional<CFileLogOssTypeEnum> getEnumByValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }

}
