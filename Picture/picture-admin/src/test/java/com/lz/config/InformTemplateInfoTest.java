package com.lz.config;

import com.lz.common.utils.StringUtils;
import com.lz.config.model.domain.InformTemplateInfo;
import com.lz.config.service.IInformTemplateInfoService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

/**
 * Project: Picture
 * Package: com.lz.config
 * Author: YY
 * CreateTime: 2025-03-15  15:41
 * Description: InformTemplateInfoTest
 * Version: 1.0
 */
@SpringBootTest
public class InformTemplateInfoTest {

    @Resource
    private IInformTemplateInfoService informTemplateInfoService;

    @Test
    public void test() {
        System.out.println("test");
    }

    /**
     * description: 测试替换内容和变量模版为事例
     * author: YY
     * method: testChangeTemplateExample
     * date: 2025/3/15 15:56
     * param:
     * return: void
     **/
    @Test
    public void testChangeTemplateExample() {
        String content = "你好我是${userName},我想对你说${tell},你是谁${you}";
        HashMap<String, String> params = new HashMap<>();
        params.put("userName", "YY");
        params.put("tell", "你好");
        String format = StringUtils.parseTemplate(content, params);
        System.out.println(format);

        String variables = "{\"userName\":\"姓名\",\"tell\":\"内容\",\"you\":\"你是谁\"}";
        InformTemplateInfo informTemplateInfo = new InformTemplateInfo();
        informTemplateInfo.setVariables(variables);
        informTemplateInfo.setContent("");
        String example = informTemplateInfoService.getExample(informTemplateInfo);
        System.out.println(example);
    }
}
