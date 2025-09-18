package com.lz.picture.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lz.common.core.domain.statistics.ro.StatisticsRo;
import com.lz.picture.model.domain.StatisticsInfo;
import com.lz.picture.model.dto.statistics.KeywordStatisticsRequest;

import java.util.List;

/**
 * 统计信息Mapper接口
 *
 * @author YY
 * @date 2025-07-20
 */
public interface StatisticsInfoMapper extends BaseMapper<StatisticsInfo> {
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
     * 删除统计信息
     *
     * @param statisticsId 统计信息主键
     * @return 结果
     */
    public int deleteStatisticsInfoByStatisticsId(String statisticsId);

    /**
     * 批量删除统计信息
     *
     * @param statisticsIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStatisticsInfoByStatisticsIds(String[] statisticsIds);

    /**
     * 用户搜索关键词统计
     *
     * @param request 请求参数
     * @return List<StatisticsRo>
     * @author: YY
     * @method: keywordSearchStatistics
     * @date: 2025/9/18 16:30
     **/
    List<StatisticsRo> keywordSearchStatistics(KeywordStatisticsRequest request);

    /**
     * 图片标签关键词统计-热门标签
     *
     * @param request 请求
     * @return List<StatisticsRo>
     * @author: YY
     * @method: tagKeywordStatistics
     * @date: 2025/9/18 18:23
     **/
    List<StatisticsRo> tagKeywordStatistics(KeywordStatisticsRequest request);
}
