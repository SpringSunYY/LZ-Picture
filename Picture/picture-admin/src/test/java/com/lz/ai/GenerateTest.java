package com.lz.ai;

import com.lz.ai.model.enums.AiModelParamsTypeEnum;
import com.lz.ai.service.IGenerateLogInfoService;
import com.lz.ai.strategy.generate.domain.AiGenerateRequest;
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
        AiGenerateRequest request = new AiGenerateRequest();
        request.setUserId("009");
        request.setUsername("YYYY");
        request.setModelKeys(List.of("JiMeng_CVSync2AsyncSubmitTask"));
        request.setModelType(AiModelParamsTypeEnum.MODEL_PARAMS_TYPE_1.getValue());
        request.setInputFile(null);
        request.setPrompt("采用细腻的半厚涂画风，展现一位气质阴郁的男子形象。他的浅金色中长发显得有些散乱，在俯视且近距离的视角构图中，画面聚焦于他的面部，对其面部细节精心描绘，尤其是那浓密纤长的睫毛十分吸睛，给人一种神秘的感觉。瞳孔是浅蓝色");
        request.setNegativePrompt(null);
        request.setSeed(1.0f);
        request.setNumbers(4);
        request.setWidth(1024);
        request.setHeight(628);
        request.setBrowser("Chrome");
        request.setOs("Windows");
        request.setPlatform("PC");
        request.setIpAddr("192.168.1.1");
        request.setDeviceId("1234567890");
        generateLogInfoService.userGenerate(request);
    }

    @Test
    public void testJMQuery() throws Exception {
        generateLogInfoService.queryTask("42c5313eeae643d7b45cda3cb6f07369", "1", "admin");
    }
}
