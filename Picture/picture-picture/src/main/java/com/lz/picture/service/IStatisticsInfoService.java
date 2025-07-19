package com.lz.picture.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lz.picture.model.domain.StatisticsInfo;
import com.lz.picture.model.dto.statisticsInfo.StatisticsInfoQuery;
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
}
