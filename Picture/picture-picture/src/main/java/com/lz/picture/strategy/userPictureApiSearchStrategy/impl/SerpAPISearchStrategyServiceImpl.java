package com.lz.picture.strategy.userPictureApiSearchStrategy.impl;

import cn.hutool.http.HttpUtil;
import com.lz.picture.model.dto.pictureApiSearch.PictureApiSearchRequest;
import com.lz.picture.model.vo.pictureApiSearch.PictureApiSearchVo;
import com.lz.picture.strategy.userPictureApiSearchStrategy.UserPictureApiSearchStrategyConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * SerpAPI 搜索策略实现类
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-16  16:17
 * @Version: 1.0
 */
@UserPictureApiSearchStrategyConfig(api = "serpApi")
public class SerpAPISearchStrategyServiceImpl extends UserPictureApiSearchStrategyTemplate {
    @Override
    public PictureApiSearchVo keywordSearch(PictureApiSearchRequest pictureApiSearchRequest) {
        //首先先校验
        verifyParams(pictureApiSearchRequest);
        //请求数据
        /*
        params = {
            "q": "坤坤表情包",
            "location": "Austin, Texas, United States",
            "hl": "en",
            "gl": "us",
            "google_domain": "google.com",
            "engine": "google_images",
            "api_key": "dfa9ecffec314513be0274de3635ad77046bfc77b6f8d784523d12e3d1079172"  # 用你的真实 key 替换
        }
         */
        Map<String, Object> params = new HashMap<>();
        params.put("q", pictureApiSearchRequest.getKeyword());
        params.put("location","Austin, Texas, United States");
        params.put("hl","en");
        params.put("gl","us");
        params.put("google_domain","google.com");
        params.put("engine","bing_images");
        params.put("api_key", "dfa9ecffec314513be0274de3635ad77046bfc77b6f8d784523d12e3d1079172");
        String string = HttpUtil.get("https://serpapi.com/search", params);
        System.out.println("string = " + string);
        return new PictureApiSearchVo();
    }
}
