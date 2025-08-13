package com.lz.ai.strategy.generate;

import com.lz.ai.model.domain.GenerateLogInfo;
import com.lz.ai.model.domain.ModelParamsInfo;
import com.lz.ai.model.enums.AiModelParamsStatusEnum;
import com.lz.ai.service.IModelParamsInfoService;
import com.lz.ai.strategy.generate.domain.AiGenerateRequest;
import com.lz.ai.strategy.generate.domain.dto.GenerateLogInfoDto;
import com.lz.common.constant.HttpStatus;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * AI生成策略模式执行器
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-08  23:42
 * @Version: 1.0
 */
@Service
public class AiGenerateStrategyExecutor {
    @Resource
    private IModelParamsInfoService modelParamsInfoService;

    @Resource
    private List<AiGenerateStrategyService> aiGenerateStrategyServiceList;


    /**
     * 用户生成执行
     *
     * @param request 请求参数
     * @return String
     * @author: YY
     * @method: executeUserGenerate
     * @date: 2025/8/9 00:25
     **/
    public List<GenerateLogInfo> executeUserGenerate(AiGenerateRequest request) {
        //首先查询到所有的模型参数
        List<String> modelKeys = request.getModelKeys();
        List<GenerateLogInfoDto> generateLogInfoDtos = new ArrayList<>();
        ArrayList<GenerateLogInfo> logInfos = new ArrayList<>();
        for (String modelKey : modelKeys) {
            //查询每个key是否存在
            ModelParamsInfo modelParamsInfo = modelParamsInfoService.selectModelParamsInfoByModelKey(modelKey);
            ThrowUtils.throwIf(StringUtils.isNull(modelParamsInfo) ||
                            !modelParamsInfo.getParamsStatus().equals(AiModelParamsStatusEnum.MODEL_PARAMS_STATUS_0.getValue()),
                    HttpStatus.NO_CONTENT,
                    "模型参数不存在或者未启用");
            GenerateLogInfoDto generateLogInfoDto = new GenerateLogInfoDto(request, modelParamsInfo);
            generateLogInfoDtos.add(generateLogInfoDto);
        }
        StringBuilder buffer = new StringBuilder();
        for (GenerateLogInfoDto info : generateLogInfoDtos) {
            //遍历执行，拿到对应的执行器
            for (AiGenerateStrategyService strategyService : aiGenerateStrategyServiceList) {
                AiGenerateStrategyConfig annotation = strategyService.getClass().getAnnotation(AiGenerateStrategyConfig.class);
                if (annotation.model().equals(info.getModel())) {
                    List<GenerateLogInfo> generateLogInfos = strategyService.userGenerate(info);
                    logInfos.addAll(generateLogInfos);
                }
            }
        }
        return logInfos;
    }

    public GenerateLogInfo executeQuery(GenerateLogInfo generateLogInfo, String username) {
        ModelParamsInfo modelParamsInfo = modelParamsInfoService.selectModelParamsInfoByModelKey(generateLogInfo.getModelKey());
        ThrowUtils.throwIf(StringUtils.isNull(modelParamsInfo),
                HttpStatus.NO_CONTENT,
                "模型参数不存在或者未启用");
        for (AiGenerateStrategyService aiGenerateStrategyService : aiGenerateStrategyServiceList) {
            AiGenerateStrategyConfig annotation = aiGenerateStrategyService.getClass().getAnnotation(AiGenerateStrategyConfig.class);
            if (annotation.model().equals(modelParamsInfo.getModel())) {
                return aiGenerateStrategyService.query(generateLogInfo, modelParamsInfo, username);
            }
        }
        return null;
    }
}
