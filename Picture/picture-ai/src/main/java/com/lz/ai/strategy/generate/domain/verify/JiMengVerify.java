package com.lz.ai.strategy.generate.domain.verify;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 即梦参数校验
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-09  16:15
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class JiMengVerify extends Verify<JiMengVerify> {
    @Override
    public String objToJson(JiMengVerify verify) {
        return JSONObject.toJSONString(verify);
    }

    @Override
    public JiMengVerify jsonToObj(String json) {
        return JSONObject.parseObject(json, JiMengVerify.class);
    }

}
