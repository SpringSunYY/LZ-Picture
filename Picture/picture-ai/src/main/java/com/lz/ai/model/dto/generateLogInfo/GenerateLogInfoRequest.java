package com.lz.ai.model.dto.generateLogInfo;

import com.lz.ai.model.domain.GenerateLogInfo;
import jakarta.validation.constraints.NotNull;
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
public class GenerateLogInfoRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 模型KEY
     */
    @NotNull(message = "模型KEY不能为空")
    private List<String> modelKeys;

    /**
     * 模型类型
     */
    @NotNull(message = "模型类型不能为空")
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
    private Long seed;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    private Long numbers;


    /**
     * 宽度
     */
    @NotNull(message = "宽度不能为空")
    private Long width;

    /**
     * 高度
     */
    @NotNull(message = "高度不能为空")
    private Long height;


    /**
     * 参考对象
     */
    private String targetId;


    /**
     * 对象转封装类
     *
     * @param generateLogInfoInsert 插入对象
     * @return GenerateLogInfoInsert
     */
    public static GenerateLogInfo insertToObj(GenerateLogInfoRequest generateLogInfoInsert) {
        if (generateLogInfoInsert == null) {
            return null;
        }
        GenerateLogInfo generateLogInfo = new GenerateLogInfo();
        BeanUtils.copyProperties(generateLogInfoInsert, generateLogInfo);
        return generateLogInfo;
    }
}
