package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.StatisticsInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 统计信息Mapper接口
 *
 * @author YY
 * @date 2025-07-17
 */
public interface StatisticsInfoMapper extends BaseMapper<StatisticsInfo>
{
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
}
