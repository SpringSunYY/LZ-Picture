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
@UserPictureApiSearchStrategyConfig(api = "pixabayApi")
public class PixabayAPISearchStrategyServiceImpl extends UserPictureApiSearchStrategyTemplate {
    @Override
    public PictureApiSearchVo keywordSearch(PictureApiSearchRequest pictureApiSearchRequest) {
        //首先先校验
        verifyParams(pictureApiSearchRequest);
        //请求数据
        /*
      params = {
            "key": API_KEY,
            "q": q,
            "image_type": "photo",
            "orientation": "horizontal",
            "per_page": 200,  # 每页最多返回 200
            "safesearch": "true"
        }
         */
        Map<String, Object> params = new HashMap<>();
        params.put("q", pictureApiSearchRequest.getKeyword());
        params.put("image_type", "photo");
        params.put("orientation", "all");
        params.put("per_page", "200");
        params.put("google_domain", "google.com");
        params.put("engine", "bing_images");
        params.put("key", "50872063-1b1c978ffd0b79eafc58cbb99");
        String string = HttpUtil.get("https://pixabay.com/api/", params);
        System.out.println("string = " + string);
        return new PictureApiSearchVo();
    }
}
