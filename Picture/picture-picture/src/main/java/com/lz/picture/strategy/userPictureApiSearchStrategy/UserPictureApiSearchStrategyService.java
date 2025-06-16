package com.lz.picture.strategy.userPictureApiSearchStrategy;

import com.lz.picture.model.dto.pictureApiSearch.PictureApiSearchRequest;
import com.lz.picture.model.vo.pictureApiSearch.PictureApiSearchVo;

/**
 * 用户图片搜索策略
 *
 * @author YY
 */
public interface UserPictureApiSearchStrategyService {
    /**
     * 根据关键词搜索
     *
     * @param pictureApiSearchRequest
     * @return
     */
    PictureApiSearchVo keywordSearch(PictureApiSearchRequest pictureApiSearchRequest);
}
