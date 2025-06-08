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
     * 获取用户浏览推荐分数
     *
     * @param userId         用户编号
     * @param targetType     目标类型
     * @param limit          记录数
     * @param categoryWeight 分类权重
     * @param tagWeight      标签权重
     * @param timeDecay      时间衰减
     * @return UserInterestModel
     * @author: YY
     * @method: getUserViewInterest
     * @date: 2025/6/6 22:14
     **/
    public UserInterestModel getUserViewInterest(String userId, String targetType, Integer limit, Integer categoryWeight, Integer tagWeight, Double timeDecay);

    /**
     * 获取用户行为推荐分数
     *
     * @param userId         用户编号
     * @param targetType     目标类型
     * @param limit          记录数
     * @param categoryWeight 分类权重
     * @param tagWeight      标签权重
     * @param timeDecay      时间衰减
     * @return UserInterestModel
     * @author: YY
     * @method: getUserBehaviorInterest
     * @date: 2025/6/6 22:54
     **/
    public UserInterestModel getUserBehaviorInterest(String userId, String targetType, Integer limit, Integer categoryWeight, Integer tagWeight, Double timeDecay);

    /**
     * 获取用户图片下载记录分数
     *
     * @param userId
     * @param limit
     * @param categoryWeight
     * @param tagWeight
     * @param timeDecay
     * @return UserInterestModel
     * @author: YY
     * @method: getPictureDownloadInterest
     * @date: 2025/6/6 22:57
     **/
    public UserInterestModel getPictureDownloadInterest(String userId, Integer limit, Integer categoryWeight, Integer tagWeight, Double timeDecay);

    /**
     * 获取用户推荐分数
     *
     * @param userId 用户编号
     * @return UserInterestModel
     * @author: YY
     * @method: getUserInterest
     * @date: 2025/6/6 23:43
     **/
    public UserInterestModel getUserInterest(String userId);


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
