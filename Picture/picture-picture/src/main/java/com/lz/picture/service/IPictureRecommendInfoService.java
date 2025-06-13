package com.lz.picture.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lz.picture.model.domain.PictureRecommendInfo;
import com.lz.picture.model.dto.pictureRecommend.UserInterestModel;
import com.lz.picture.model.dto.pictureRecommendInfo.PictureRecommendInfoQuery;
import com.lz.picture.model.vo.pictureRecommendInfo.PictureRecommendInfoVo;

import java.util.List;

/**
 * 用户图片推荐模型Service接口
 *
 * @author YY
 * @date 2025-06-10
 */
public interface IPictureRecommendInfoService extends IService<PictureRecommendInfo> {
    //region mybatis代码

    /**
     * 查询用户图片推荐模型
     *
     * @param recommendId 用户图片推荐模型主键
     * @return 用户图片推荐模型
     */
    public PictureRecommendInfo selectPictureRecommendInfoByRecommendId(String recommendId);

    /**
     * 查询用户图片推荐模型列表
     *
     * @param pictureRecommendInfo 用户图片推荐模型
     * @return 用户图片推荐模型集合
     */
    public List<PictureRecommendInfo> selectPictureRecommendInfoList(PictureRecommendInfo pictureRecommendInfo);

    /**
     * 新增用户图片推荐模型
     *
     * @param pictureRecommendInfo 用户图片推荐模型
     * @return 结果
     */
    public int insertPictureRecommendInfo(PictureRecommendInfo pictureRecommendInfo);

    /**
     * 修改用户图片推荐模型
     *
     * @param pictureRecommendInfo 用户图片推荐模型
     * @return 结果
     */
    public int updatePictureRecommendInfo(PictureRecommendInfo pictureRecommendInfo);

    /**
     * 批量删除用户图片推荐模型
     *
     * @param recommendIds 需要删除的用户图片推荐模型主键集合
     * @return 结果
     */
    public int deletePictureRecommendInfoByRecommendIds(String[] recommendIds);

    /**
     * 删除用户图片推荐模型信息
     *
     * @param recommendId 用户图片推荐模型主键
     * @return 结果
     */
    public int deletePictureRecommendInfoByRecommendId(String recommendId);
    //endregion

    /**
     * 获取查询条件
     *
     * @param pictureRecommendInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PictureRecommendInfo> getQueryWrapper(PictureRecommendInfoQuery pictureRecommendInfoQuery);

    /**
     * 转换vo
     *
     * @param pictureRecommendInfoList PictureRecommendInfo集合
     * @return PictureRecommendInfoVO集合
     */
    List<PictureRecommendInfoVo> convertVoList(List<PictureRecommendInfo> pictureRecommendInfoList);

    public UserInterestModel getUserInterrestModelByUserId(String userId);

    /**
     * 插入用户推荐模型
     *
     * @param userId
     * @param type
     * @return void
     * @author: YY
     * @method: insertUserInterestModel
     * @date: 2025/6/13 21:47
     **/
    void insertUserInterestModel(String userId, String type);

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
     * @date: 2025/6/10 11:40
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
     * @date: 2025/6/10 11:40
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
     * @date: 2025/6/10 11:40
     **/
    public UserInterestModel getPictureDownloadInterest(String userId, Integer limit, Integer categoryWeight, Integer tagWeight, Double timeDecay);

    /**
     * 获取用户推荐分数
     *
     * @param userId 用户编号
     * @return UserInterestModel
     * @author: YY
     * @method: getUserInterest
     * @date: 2025/6/10 11:40
     **/
    public UserInterestModel getUserInterest(String userId);
}
