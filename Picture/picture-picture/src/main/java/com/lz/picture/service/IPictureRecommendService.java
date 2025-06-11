package com.lz.picture.service;

import com.lz.picture.model.dto.pictureRecommend.PictureRecommendRequest;
import com.lz.picture.model.dto.pictureRecommend.UserInterestModel;
import com.lz.picture.model.vo.pictureInfo.UserRecommendPictureInfoVo;

import java.util.List;

/**
 * 用户推荐服务
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-06  20:41
 * @Version: 1.0
 */
public interface IPictureRecommendService {
    /**
     * 获取图片详情推荐
     * @author: YY
     * @method: getPictureInfoRecommend
     * @date: 2025/6/5 23:20
     * @param pictureRecommendRequest 图片推荐请求
     * @return List<UserPictureInfoVo>
     **/
    public List<UserRecommendPictureInfoVo>getPictureInfoRecommend(PictureRecommendRequest pictureRecommendRequest);
}
