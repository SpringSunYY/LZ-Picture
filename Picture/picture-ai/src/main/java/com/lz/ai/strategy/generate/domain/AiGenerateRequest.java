package com.lz.ai.strategy.generate.domain;

import com.lz.ai.model.domain.GenerateLogInfo;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 用户请求生成对象
 * 理想是前行的舟船，现在就是小舟
 *
 * @author YY
 * @date 2025-08-08
 */
@Data
public class AiGenerateRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 模型KEY
     */
    @NotEmpty(message = "请选择模型")
    private List<String> modelKeys;

    /**
     * 模型类型
     */
    @NotBlank(message = "请选择模型类型")
    private String modelType;

    /**
     * 输入文件
     */
    private String inputFile;

    /**
     * 提示词
     */
    private String prompt;

    /**
     * 负向提示词
     */
    private String negativePrompt;

    /**
     * 随机种子
     */
    private Float seed;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    private Integer numbers;


    /**
     * 宽度
     */
    @NotNull(message = "宽度不能为空")
    @Min(value = 256, message = "最小宽度为256")
    @Max(value = 3024, message = "最大宽度为3024")
    private Integer width;

    /**
     * 高度
     */
    @NotNull(message = "高度不能为空")
    @Min(value = 256, message = "最小高度为256")
    @Max(value = 3024, message = "最大高度为3024")
    private Integer height;


    /**
     * 参考对象
     */
    private String targetId;


    /**
     * 用户IP地址
     */
    private String ipAddr;

    /**
     * 用户设备唯一标识
     */
    private String deviceId;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 平台
     */
    private String platform;


    /**
     * 对象转封装类
     *
     * @param generateLogInfoInsert 插入对象
     * @return GenerateLogInfoInsert
     */
    public static GenerateLogInfo insertToObj(AiGenerateRequest generateLogInfoInsert) {
        if (generateLogInfoInsert == null) {
            return null;
        }
        GenerateLogInfo generateLogInfo = new GenerateLogInfo();
        BeanUtils.copyProperties(generateLogInfoInsert, generateLogInfo);
        return generateLogInfo;
    }
}
