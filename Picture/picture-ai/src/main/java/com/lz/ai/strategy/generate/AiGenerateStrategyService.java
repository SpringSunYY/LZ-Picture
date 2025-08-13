package com.lz.ai.strategy.generate;

import com.lz.ai.model.domain.GenerateLogInfo;
import com.lz.ai.model.domain.ModelParamsInfo;
import com.lz.ai.strategy.generate.domain.dto.GenerateLogInfoDto;

import java.util.List;

/**
 * AI生成策略模式执行器
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-08  23:42
 * @Version: 1.0
 */
public interface AiGenerateStrategyService {
    /**
     * 用户生成
     *
     * @param info
     * @return String
     * @author: YY
     * @method: userGenerate
     * @date: 2025/8/9 00:25
     **/
    List<GenerateLogInfo> userGenerate(GenerateLogInfoDto info);

    /**
     * 查询任务列表
     */
    public GenerateLogInfo query(GenerateLogInfo query, ModelParamsInfo modelParamsInfo, String username);
}
