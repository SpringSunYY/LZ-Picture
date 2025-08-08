package com.lz.ai.strategy.generate;

import com.lz.ai.model.dto.generateLogInfo.GenerateLogInfoDto;

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
    String userGenerate(GenerateLogInfoDto info);
}
