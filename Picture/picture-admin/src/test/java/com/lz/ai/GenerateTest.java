package com.lz.ai;

import com.lz.ai.model.dto.generateLogInfo.GenerateLogInfoRequest;
import com.lz.ai.model.enums.AiModelParamsTypeEnum;
import com.lz.ai.service.IGenerateLogInfoService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 生成测试
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-09  18:12
 * @Version: 1.0
 */
@SpringBootTest
public class GenerateTest {
    @Resource
    private IGenerateLogInfoService generateLogInfoService;

    @Test
    public void testGenerate() {
        GenerateLogInfoRequest request = new GenerateLogInfoRequest();
        request.setUserId("1");
        request.setModelKeys(List.of("JiMeng_CVSync2AsyncSubmitTask"));
        request.setModelType(AiModelParamsTypeEnum.MODEL_PARAMS_TYPE_1.getValue());
        request.setInputFile(null);
        request.setPrompt("头像插画 五官特写 极致光影 逆光 白色长发 少女 45度仰望天空 蓝色纯色背景 笔刷画 笔触质感 虚焦 颗粒感 朦胧感 复古 做旧 极简主义");
        request.setNegativePrompt(null);
        request.setSeed(1.0f);
        request.setNumbers(1);
        request.setWidth(1024);
        request.setHeight(628);
        generateLogInfoService.userGenerate(request);
    }
}
