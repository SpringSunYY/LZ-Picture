package com.lz.ai.strategy.generate.domain;

import com.lz.ai.model.domain.GenerateLogInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 用户请求生成对象--查询任务
 * 理想是前行的舟船，现在就是小舟
 *
 * @author YY
 * @date 2025-08-08
 */
@Data
public class AiGenerateQuery implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 模型KEY
     */
    private String modelKeys;


    /**
     * 参考对象
     */
    private String targetId;

    /**
     * 模型KEY
     */
    private String modelKey;

    /**
     * 任务编号
     */
    private String taskId;

    /**
     * 价格
     */
    private BigDecimal priceUse;

    /**
     * 积分
     */
    private Long pointsNeed;

    /**
     * 模型名称
     */
    private String modelName;


    /**
     * 模型
     */
    private String model;


    /**
     * API地址
     */
    private String apiUrl;

    /**
     * 安全密钥
     */
    private String apiKey;

    /**
     * 安全KEY
     */
    private String secretKey;

    /**
     * 模型参数
     */
    private String modelParams;
}
