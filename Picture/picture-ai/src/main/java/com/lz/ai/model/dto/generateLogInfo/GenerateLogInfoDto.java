package com.lz.ai.model.dto.generateLogInfo;

import com.lz.ai.model.domain.ModelParamsInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
public class GenerateLogInfoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 模型KEY
     */
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
    private Float seed;

    /**
     * 数量
     */
    private Integer numbers;


    /**
     * 宽度
     */
    private Integer width;

    /**
     * 高度
     */
    private Integer height;


    /**
     * 参考对象
     */
    private String targetId;

    /**
     * 模型KEY
     */
    private String modelKey;

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


    /**
     * 积分
     */
    private Long pointsNeed;

    /**
     * 扩展配置
     */
    private String extendConfig;


    public GenerateLogInfoDto(GenerateLogInfoRequest request, ModelParamsInfo modelParamsInfo) {
        this.userId = request.getUserId();
        this.modelKeys = request.getModelKeys();
        this.modelType = request.getModelType();
        this.inputFile = request.getInputFile();
        this.prompt = request.getPrompt();
        this.negativePrompt = request.getNegativePrompt();
        this.seed = request.getSeed();
        this.numbers = request.getNumbers();
        this.width = request.getWidth();
        this.height = request.getHeight();
        this.targetId = request.getTargetId();
        this.modelKey = modelParamsInfo.getModelKey();
        this.modelName = modelParamsInfo.getModelName();
        this.model = modelParamsInfo.getModel();
        this.apiUrl = modelParamsInfo.getApiUrl();
        this.apiKey = modelParamsInfo.getApiKey();
        this.secretKey = modelParamsInfo.getSecretKey();
        this.modelParams = modelParamsInfo.getModelParams();
        this.pointsNeed = modelParamsInfo.getPointsNeed();
        this.extendConfig = modelParamsInfo.getExtendConfig();
        this.model = modelParamsInfo.getModel();
    }

    public GenerateLogInfoDto() {
    }
}
