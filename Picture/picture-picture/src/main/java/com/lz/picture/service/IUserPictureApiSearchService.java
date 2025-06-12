package com.lz.picture.service;

import com.lz.picture.model.dto.pictureApiSearch.PictureApiSearchRequest;
import com.lz.picture.model.vo.pictureApiSearch.PictureApiSearchVo;

/**
 * 用户图片搜索服务 api
 */
public interface IUserPictureApiSearchService {
    /**
     * 关键词搜索图片
     *
     * @param pictureApiSearchRequest
     * @return PictureApiSearchVo
     * @author: YY
     * @method: keyword  关键词搜索
     * @date: 2025/6/12 23:45
     **/
    PictureApiSearchVo keyword(PictureApiSearchRequest pictureApiSearchRequest);
}
