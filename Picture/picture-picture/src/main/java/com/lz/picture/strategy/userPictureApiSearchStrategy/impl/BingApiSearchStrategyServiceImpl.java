package com.lz.picture.strategy.userPictureApiSearchStrategy.impl;

import com.lz.picture.model.dto.pictureApiSearch.PictureApiSearchRequest;
import com.lz.picture.model.vo.pictureApiSearch.PictureApiSearchVo;
import com.lz.picture.strategy.userPictureApiSearchStrategy.UserPictureApiSearchStrategyConfig;

/**
 * bing api搜索策略实现类
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-16  15:48
 * @Version: 1.0
 */
@UserPictureApiSearchStrategyConfig(api = "bing")
public class BingApiSearchStrategyServiceImpl extends UserPictureApiSearchStrategyTemplate {
    @Override
    public PictureApiSearchVo keywordSearch(PictureApiSearchRequest pictureApiSearchRequest) {
        return super.keywordSearch(pictureApiSearchRequest);
    }
}
