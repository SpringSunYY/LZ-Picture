package com.lz.ai.strategy.generate.domain.params;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 即梦参数
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-09  15:36
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class JiMengParams extends Params<JiMengParams> {
    //算法名称
    private String req_key;

    //开启文本扩写
    private Boolean use_pre_llm;

    //随机种子
    private Integer seed;

    //接口名
    private String action;
    private String queryAction;

    //版本号
    private String version;

    private String method;

    private boolean return_url;

    private String region;
    private String service;
    private String schema;
    private String host;
    private String path;
    private String ak;
    private String sk;
    private String task_id;

    private String req_json;

    private String[] image_urls;

    private String[] binary_data_base64;

    @Data
    public static class ReqJson {
        private boolean return_url;
    }

    @Override
    public JiMengParams jsonToObj(String json) {
        return JSONObject.parseObject(json, JiMengParams.class);
    }

    @Override
    public String objToJson(JiMengParams params) {
        return JSONObject.toJSONString(params);
    }
}
