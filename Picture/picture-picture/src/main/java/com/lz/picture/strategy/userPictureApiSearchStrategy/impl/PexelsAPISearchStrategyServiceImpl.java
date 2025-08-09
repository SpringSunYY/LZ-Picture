package com.lz.picture.strategy.userPictureApiSearchStrategy.impl;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.lz.picture.model.dto.pictureApiSearch.PictureApiSearchRequest;
import com.lz.picture.model.vo.pictureApiSearch.PictureApiSearchVo;
import com.lz.picture.strategy.userPictureApiSearchStrategy.UserPictureApiSearchStrategyConfig;
import com.lz.picture.strategy.userPictureApiSearchStrategy.dto.PexelsResponse;

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
@UserPictureApiSearchStrategyConfig(api = "pexelsApi")
public class PexelsAPISearchStrategyServiceImpl extends UserPictureApiSearchStrategyTemplate {
    @Override
    public PictureApiSearchVo keywordSearch(PictureApiSearchRequest pictureApiSearchRequest) {
        // 首先先校验
        verifyParams(pictureApiSearchRequest);

        // 请求数据
        Map<String, Object> params = new HashMap<>();
        params.put("query", pictureApiSearchRequest.getKeyword());
        params.put("per_page", 2); // 每页最大支持 80
        params.put("page", 1);

        String url = "https://api.pexels.com/v1/search";

        // 使用 HttpRequest 构建带 header 的请求
        String string = HttpRequest.get(url)
                .form(params) // 设置查询参数
                .header("Authorization", "2GaBnlATi1iqlGUCPNjTwVwZnQ1F74PxNkYXPH1liWARuRgDcwSj3kHJ") // 设置请求头
                .execute()
                .body();

        PexelsResponse pexelsResponse = JSONObject.parseObject(string, PexelsResponse.class);
        System.out.println("pexelsResponse = " + pexelsResponse);
        return new PictureApiSearchVo();
    }
}
