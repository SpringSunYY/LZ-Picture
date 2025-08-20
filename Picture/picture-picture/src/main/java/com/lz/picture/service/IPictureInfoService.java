package com.lz.picture.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lz.common.core.page.TableDataInfo;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.dto.pictureDownloadLogInfo.PictureDownloadLogInfoRequest;
import com.lz.picture.model.dto.pictureInfo.*;
import com.lz.picture.model.vo.pictureInfo.*;

import java.util.List;

/**
 * 图片信息Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface IPictureInfoService extends IService<PictureInfo> {
    //region mybatis代码

    /**
     * 查询图片信息
     *
     * @param pictureId 图片信息主键
     * @return 图片信息
     */
    public PictureInfo selectPictureInfoByPictureId(String pictureId);

    /**
     * 查询图片信息列表
     *
     * @param pictureInfo 图片信息
     * @return 图片信息集合
     */
    public List<PictureInfo> selectPictureInfoList(PictureInfo pictureInfo);

    /**
     * 新增图片信息
     *
     * @param pictureInfo 图片信息
     * @return 结果
     */
    public int insertPictureInfo(PictureInfo pictureInfo);

    /**
     * 修改图片信息
     *
     * @param pictureInfo 图片信息
     * @return 结果
     */
    public int updatePictureInfo(PictureInfo pictureInfo);

    /**
     * 批量删除图片信息
     *
     * @param pictureIds 需要删除的图片信息主键集合
     * @return 结果
     */
    public int deletePictureInfoByPictureIds(String[] pictureIds);

    /**
     * 删除图片信息信息
     *
     * @param pictureId 图片信息主键
     * @return 结果
     */
    public int deletePictureInfoByPictureId(String pictureId);
    //endregion

    /**
     * 获取查询条件
     *
     * @param pictureInfo 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PictureInfo> getQueryWrapper(PictureInfo pictureInfo);

    /**
     * 转换vo
     *
     * @param pictureInfoList PictureInfo集合
     * @return PictureInfoVO集合
     */
    List<PictureInfoVo> convertVoList(List<PictureInfo> pictureInfoList);

    /**
     * 用户新增图片信息
     *
     * @param pictureInfo
     * @return
     */
    int userInsertPictureInfo(PictureInfo pictureInfo);

    /**
     * description: 查询图片详细信息，包括空间、用户、标签、文件夹
     * author: YY
     * method: userSelectPictureInfoByPictureId
     * date: 2025/4/11 15:47
     * param:
     * param: pictureId
     * return: com.lz.picture.model.vo.pictureInfo.UserPictureDetailInfoVo
     **/
    UserPictureDetailInfoVo userSelectPictureInfoByPictureId(String pictureId, String userId);

    /**
     * description: 重新设置缓存,根据行为
     * author: YY
     * method: resetPictureInfoCache
     * date: 2025/4/15 00:04
     * param:
     * param: pictureId 图片id
     * return: void
     **/
    void resetPictureInfoCacheByBehavior(String pictureId, String behaviorType, Boolean exist);

    /**
     * 用户自己查看图片信息
     *
     * @param pictureId 图片id
     * @param userId    用户ID
     * @return com.lz.picture.model.vo.pictureInfo.UserPictureDetailInfoVo
     * @author YY
     * @method userMySelectPictureInfoByPictureId
     * @date 2025/4/23 23:03
     **/
    UserPictureDetailInfoVo userMySelectPictureInfoByPictureId(String pictureId, String userId);


    /**
     * 用户更新图片信息
     *
     * @param pictureInfo 图片信息
     * @return int
     * @author YY
     * @method userUpdatePictureInfo
     * @date 2025/4/26 20:40
     **/
    UserPictureDetailInfoVo userUpdatePictureInfo(PictureInfo pictureInfo);

    /**
     * 校验图片
     *
     * @param pictureId    图片编号
     * @param userId       用户编号
     * @param downloadType 下载类型
     * @return PictureInfo
     * @author: YY
     * @method: verifyPictureInfo
     * @date: 2025/5/24 20:30
     **/
    PictureInfoDto verifyPictureInfo(String pictureId, String userId, String downloadType);

    /**
     * 用户自己下载图片
     *
     * @param pictureId 图片编号
     * @param userId    用户编号
     * @return PictureInfoDto
     * @author: YY
     * @method: verifyPictureInfoByMy
     * @date: 2025/8/1 22:49
     **/
    PictureInfoDto verifyPictureInfoByMy(String pictureId, String userId);

    /**
     * 用户根据自己下载记录校验图片
     *
     * @param pictureDownloadLogInfoRequest 图片下载记录信息
     * @return PictureInfo
     * @author: YY
     * @method: verifyPictureInfoByLog
     * @date: 2025/6/2 16:09
     **/
    PictureInfoDto verifyPictureInfoByLog(PictureDownloadLogInfoRequest pictureDownloadLogInfoRequest);

    /**
     * 获取图片搜索建议
     *
     * @param
     * @return List<PictureInfoSearchSuggestionVo>
     * @author: YY
     * @method: getSearchSuggestion
     * @date: 2025/6/3 23:04
     **/
    List<PictureInfoSearchRecommendVo> getSearchRecommend();

    /**
     * 获取建议
     *
     * @param name       图片名称
     * @param uploadType 上传类型
     * @return List<PictureInfoSearchSuggestionVo>
     * @author: YY
     * @method: getSearchSuggestion
     * @date: 2025/6/4 00:14
     **/
    List<PictureInfoSearchSuggestionVo> getSearchSuggestion(String name, String uploadType);

    /**
     * 根据图片详情获取推荐图片
     *
     * @param pictureInfoDetailRecommendRequest 图片推荐请求
     * @return List<UserPictureInfoVo>
     * @author: YY
     * @method: getPictureInfoDetailRecommend
     * @date: 2025/6/5 23:20
     **/
    List<UserPictureInfoVo> getPictureInfoDetailRecommend(PictureInfoDetailRecommendRequest pictureInfoDetailRecommendRequest);

    /**
     * 获取推荐热门图片
     *
     * @param request 图片推荐请求
     * @return List<UserPictureInfoVo>
     * @author: YY
     * @method: getRecommentHotPictureInfoList
     * @date: 2025/6/6 00:05
     **/
    List<UserRecommendPictureInfoVo> queryPictureInfoList(PictureQueryRequest request);

    /**
     * 获取图片表格信息
     *
     * @param userPictureInfoQuery 查询信息
     * @return Page<PictureInfoTableVo>
     * @author: YY
     * @method: listPictureInfoTable
     * @date: 2025/6/18 15:54
     **/
    TableDataInfo listPictureInfoTable(UserPictureInfoQuery userPictureInfoQuery);

    /**
     * 更新图片名称
     *
     * @param pictureInfo 图片实体
     * @return int
     * @author: YY
     * @method: userUpdatePictureInfoName
     * @date: 2025/6/18 21:55
     **/
    int userUpdatePictureInfoName(PictureInfo pictureInfo);

    /**
     * 获取图片信息正常的
     *
     * @param pictureId 图片编号
     * @return PictureInfo
     * @author: YY
     * @method: selectPictureInfoByPictureId
     * @date: 2025/6/19 00:04
     **/
    PictureInfo selectNormalPictureInfoByPictureId(String pictureId);

    /**
     * 用户删除图片
     *
     * @param pictureIds
     * @return int
     * @author: YY
     * @method: userDeletePictureInfoByIds
     * @date: 2025/6/19 20:30
     **/
    int userDeletePictureInfoByIds(String[] pictureIds);

    /**
     * 获取团队空间图片
     *
     * @param userPictureInfoQuery 团队空间
     * @return TableDataInfo
     * @author: YY
     * @method: listPictureInfoTeamTable
     * @date: 2025/7/1 19:58
     **/
    TableDataInfo listPictureInfoTeamTable(UserPictureInfoQuery userPictureInfoQuery);

    /**
     * 用户获取图片列表
     *
     * @param userPictureInfoQuery
     * @return TableDataInfo
     * @author: YY
     * @method: listMy
     * @date: 2025/7/2 00:41
     **/
    TableDataInfo listMy(UserPictureInfoQuery userPictureInfoQuery);

    /**
     * 获取热门图片列表
     *
     * @param pictureInfoHotRequest
     * @return TableDataInfo
     * @author: YY
     * @method: getPictureInfoHot
     * @date: 2025/7/14 22:56
     **/
    TableDataInfo getPictureInfoHot(PictureInfoHotRequest pictureInfoHotRequest);

    //获取图片总数
    Long getPictureCountByPictureStatus(String pictureStatus, String isDelete);

    /**
     * 获取未删除图片
     */
    PictureInfo selectPictureInfoNormalByPictureId(String pictureId);

    /**
     * 发布图片，根据AI
     *
     * @param pictureAiUpload
     * @return
     */
    int userInsertPictureInfoByAi(PictureAiUpload pictureAiUpload);

    /**
     * 查询我的AI图片
     *
     * @param query 查询条件
     * @return
     */
    TableDataInfo listAi(UserPictureInfoAiQuery query);


    /**
     * ai图片详情推荐
     * @param pictureInfoDetailRecommendRequest 查询条件
     * @return
     */
    List<PictureInfoAiDetailRecommendVo> getPictureInfoDetailRecommendByAi(PictureInfoDetailRecommendRequest pictureInfoDetailRecommendRequest);
}
