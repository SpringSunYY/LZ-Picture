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
        request.setPrompt("现代美女，白色棉布宽松吊带长裙，周围开着小雏菊，极简，俯拍视角，画面整体只有人身上有光周围是昏暗，深色长发，躺草丛里，毫无生气，近景脸部特写，暗黑风格，人物泛光，诡异美感，怪诞美学，魅惑，超高清，插画，动漫，整体与画面颜色协调，人物泛光，朦胧，动态捕捉，梦幻感，磨砂肌理绘画，笔触感，特写，精致刻画，流畅的线条，神秘感，深邃感，柔光，梦幻光影，色彩弥漫，弥散光影，色彩弥散，高级感，清冷，五官精致，高级CG，oc渲染，精致感，高质量，柔焦，大师级光影，居高临下地看向镜头，神秘感，近距离，高清，8K，动态视角 发丝细腻富有光泽，性感厚唇，柔焦，华丽光影，高雅气质，插画 ，柔和灯光，超清画质高细节，丙烯颜料，仙气，唯美造型。3d动漫厚涂，发丝细腻刻画，高清特写，朦胧，眼睛狭长，极致超清，极致细节，光影交错，光影对比强烈，光影斑斓，气质，oc渲染，超高清画质，细节丰富，精致感强，背景简约，人物特写，朦胧，二次元，细腻厚涂，极致超清");
        request.setNegativePrompt(null);
        request.setSeed(1.0f);
        request.setNumbers(4);
        request.setWidth(1024);
        request.setHeight(628);
        generateLogInfoService.userGenerate(request);
    }
}
