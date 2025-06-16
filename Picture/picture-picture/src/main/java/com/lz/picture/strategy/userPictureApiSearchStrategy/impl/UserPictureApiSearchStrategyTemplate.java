package com.lz.picture.strategy.userPictureApiSearchStrategy.impl;

import com.lz.common.constant.HttpStatus;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.picture.model.dto.pictureApiSearch.PictureApiSearchRequest;
import com.lz.picture.model.vo.pictureApiSearch.PictureApiSearchVo;
import com.lz.picture.strategy.userPictureApiSearchStrategy.UserPictureApiSearchStrategyService;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户图片搜索策略模版
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-16  14:52
 * @Version: 1.0
 */
@Slf4j
public class UserPictureApiSearchStrategyTemplate implements UserPictureApiSearchStrategyService {
    /**
     * 校验参数
     *
     * @param pictureApiSearchRequest 图片搜索请求
     * @return void
     * @author: YY
     * @method: verifyParams
     * @date: 2025/6/16 16:00
     **/
    public void verifyParams(PictureApiSearchRequest pictureApiSearchRequest) {
        ThrowUtils.throwIf(StringUtils.isNull(pictureApiSearchRequest)
                        || StringUtils.isNull(pictureApiSearchRequest.getApi())
                        || StringUtils.isNull(pictureApiSearchRequest.getCurrentPage()),
                HttpStatus.BAD_REQUEST,
                "参数错误");
    }

    @Override
    public PictureApiSearchVo keywordSearch(PictureApiSearchRequest pictureApiSearchRequest) {
        // 参数校验
        verifyParams(pictureApiSearchRequest);
        return new PictureApiSearchVo();
    }
}
