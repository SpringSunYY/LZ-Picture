package com.lz.picture.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lz.common.core.domain.statistics.vo.StatisticsVo;
import com.lz.common.core.page.TableDataInfo;
import com.lz.picture.model.domain.StatisticsInfo;
import com.lz.picture.model.dto.pictureInfo.PictureInfoHotRequest;
import com.lz.picture.model.dto.statistics.KeywordStatisticsRequest;
import com.lz.picture.model.dto.statisticsInfo.StatisticsFileDto;
import com.lz.picture.model.dto.statisticsInfo.StatisticsInfoQuery;
import com.lz.picture.model.dto.statisticsInfo.StatisticsInfoRequest;
import com.lz.picture.model.vo.statisticsInfo.StatisticsInfoVo;

import java.util.List;

/**
 * 统计信息Service接口
 *
 * @author YY
 * @date 2025-07-17
 */
public interface IStatisticsInfoService extends IService<StatisticsInfo> {
    //region mybatis代码

    /**
     * 查询统计信息
     *
     * @param statisticsId 统计信息主键
     * @return 统计信息
     */
    public StatisticsInfo selectStatisticsInfoByStatisticsId(String statisticsId);

    /**
     * 查询统计信息列表
     *
     * @param statisticsInfo 统计信息
     * @return 统计信息集合
     */
    public List<StatisticsInfo> selectStatisticsInfoList(StatisticsInfo statisticsInfo);

    /**
     * 新增统计信息
     *
     * @param statisticsInfo 统计信息
     * @return 结果
     */
    public int insertStatisticsInfo(StatisticsInfo statisticsInfo);

    /**
     * 修改统计信息
     *
     * @param statisticsInfo 统计信息
     * @return 结果
     */
    public int updateStatisticsInfo(StatisticsInfo statisticsInfo);

    /**
     * 批量删除统计信息
     *
     * @param statisticsIds 需要删除的统计信息主键集合
     * @return 结果
     */
    public int deleteStatisticsInfoByStatisticsIds(String[] statisticsIds);

    /**
     * 删除统计信息信息
     *
     * @param statisticsId 统计信息主键
     * @return 结果
     */
    public int deleteStatisticsInfoByStatisticsId(String statisticsId);
    //endregion

    /**
     * 获取查询条件
     *
     * @param statisticsInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<StatisticsInfo> getQueryWrapper(StatisticsInfoQuery statisticsInfoQuery);

    /**
     * 转换vo
     *
     * @param statisticsInfoList StatisticsInfo集合
     * @return StatisticsInfoVO集合
     */
    List<StatisticsInfoVo> convertVoList(List<StatisticsInfo> statisticsInfoList);

    /**
     * 根据统计信息获取到最新的key
     *
     * @param key 统计信息key
     * @return StatisticsInfo
     * @author: YY
     * @method: selectStatisticsInfoByStatisticsKey
     * @date: 2025/7/19 01:07
     **/
    StatisticsInfo selectStatisticsInfoByStatisticsKey(String key);

    /**
     * 获取最新的统计信息，通过公共key
     *
     * @param key 公共key
     * @return StatisticsInfo
     * @author: YY
     * @method: selectNewStatisticsInfoByCommonKey
     * @date: 2025/7/25 00:14
     **/
    StatisticsInfo selectNewStatisticsInfoByCommonKeyAndType(String key, String type);

    /**
     * 获取统计信息列表
     *
     * @param statisticKey
     * @param request      查询参数
     * @return TableDataInfo
     * @author: YY
     * @method: getStatisticsPictureInfo
     * @date: 2025/7/20 19:10
     **/
    TableDataInfo getStatisticsPictureInfo(String statisticKey, PictureInfoHotRequest request);

    /**
     * 获取统计期数
     *
     * @param request 查询信息
     * @return
     */
    Long getStatisticsInfoStages(StatisticsInfoRequest request);

    /**
     * 获取统计图片的排行文件路径
     *
     * @param type          统计类型
     * @param commonKey     公共key
     * @param statisticsKey 统计key
     * @param stage         期数
     * @param number
     * @return StatisticsFileDto
     * @author: YY
     * @method: getStatisticsPictureHotFilePath
     * @date: 2025/7/25 15:36
     **/
    StatisticsFileDto getStatisticsPictureHotFilePath(String type, String commonKey, String statisticsKey, Long stage, int number);

    /**
     * 根据日期和key获取统计信息
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param type      类型
     * @param commonKey 公共key
     * @return List<StatisticsInfo>
     * @author: YY
     * @method: getStatisticsInfosByDateAndKeyType
     * @date: 2025/9/18 16:49
     **/
    List<StatisticsInfo> getStatisticsInfosByDateAndKeyType(String startDate, String endDate, String type, String commonKey);

    /**
     * 获取统计信息最新
     *
     * @param commonKey 公共key
     * @param type      类型
     * @return StatisticsInfo
     * @author: YY
     * @method: getStatisticsInfoByCommonKey
     * @date: 2025/9/18 19:22
     **/
    StatisticsInfo getStatisticsInfoByCommonKey(String commonKey, String type);

    /**
     * 搜索记录关键词统计
     *
     * @param request 请求
     * @return List<KeywordStatisticsVo>
     * @author: YY
     * @method: keywordSearchStatistics
     * @date: 2025/9/18 16:03
     **/
    List<StatisticsVo> searchKeywordStatistics(KeywordStatisticsRequest request);

    /**
     * 图片标签关键词统计-热门标签
     *
     * @param request 请求
     * @return List<KeywordStatisticsVo>
     * @author: YY
     * @method: tagKeywordStatistics
     * @date: 2025/9/18 18:22
     **/
    List<StatisticsVo> tagKeywordStatistics(KeywordStatisticsRequest request);

    /**
     * 获取图片状态统计
     *
     * @param
     * @return List<StatisticsVo>
     * @author: YY
     * @method: pictureStatusStatistics
     * @date: 2025/9/18 19:23
     **/
    List<StatisticsVo> pictureStatusStatistics();

    /**
     * 图片上传类型统计
     *
     * @param
     * @return List<StatisticsVo>
     * @author: YY
     * @method: pictureUploadTypeStatistics
     * @date: 2025/9/18 19:24
     **/
    List<StatisticsVo> pictureUploadTypeStatistics();

    /**
     * 空间文件总数
     *
     * @return List<StatisticsVo>
     * @author: YY
     * @method: spaceFileTotalStatistics
     * @date: 2025/9/19 14:52
     **/
    List<StatisticsVo> spaceFileTotalStatistics();

    /**
     * 空间文件大小统计
     * @author: YY
     * @method: spaceFileSizeStatistics
     * @date: 2025/9/19 15:13
     * @param
     * @return List<StatisticsVo>
     **/
    List<StatisticsVo> spaceFileSizeStatistics();
}
