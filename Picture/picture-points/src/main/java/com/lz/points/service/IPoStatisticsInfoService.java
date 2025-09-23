package com.lz.points.service;

import java.util.List;
import com.lz.points.model.domain.PoStatisticsInfo;
import com.lz.points.model.vo.poStatisticsInfo.PoStatisticsInfoVo;
import com.lz.points.model.dto.poStatisticsInfo.PoStatisticsInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 统计信息Service接口
 *
 * @author YY
 * @date 2025-09-23
 */
public interface IPoStatisticsInfoService extends IService<PoStatisticsInfo>
{
    //region mybatis代码
    /**
     * 查询统计信息
     *
     * @param statisticsId 统计信息主键
     * @return 统计信息
     */
    public PoStatisticsInfo selectPoStatisticsInfoByStatisticsId(String statisticsId);

    /**
     * 查询统计信息列表
     *
     * @param poStatisticsInfo 统计信息
     * @return 统计信息集合
     */
    public List<PoStatisticsInfo> selectPoStatisticsInfoList(PoStatisticsInfo poStatisticsInfo);

    /**
     * 新增统计信息
     *
     * @param poStatisticsInfo 统计信息
     * @return 结果
     */
    public int insertPoStatisticsInfo(PoStatisticsInfo poStatisticsInfo);

    /**
     * 修改统计信息
     *
     * @param poStatisticsInfo 统计信息
     * @return 结果
     */
    public int updatePoStatisticsInfo(PoStatisticsInfo poStatisticsInfo);

    /**
     * 批量删除统计信息
     *
     * @param statisticsIds 需要删除的统计信息主键集合
     * @return 结果
     */
    public int deletePoStatisticsInfoByStatisticsIds(String[] statisticsIds);

    /**
     * 删除统计信息信息
     *
     * @param statisticsId 统计信息主键
     * @return 结果
     */
    public int deletePoStatisticsInfoByStatisticsId(String statisticsId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param poStatisticsInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PoStatisticsInfo> getQueryWrapper(PoStatisticsInfoQuery poStatisticsInfoQuery);

    /**
     * 转换vo
     *
     * @param poStatisticsInfoList PoStatisticsInfo集合
     * @return PoStatisticsInfoVO集合
     */
    List<PoStatisticsInfoVo> convertVoList(List<PoStatisticsInfo> poStatisticsInfoList);
}
