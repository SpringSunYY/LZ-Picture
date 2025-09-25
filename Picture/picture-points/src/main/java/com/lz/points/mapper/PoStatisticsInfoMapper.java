package com.lz.points.mapper;

import java.util.List;

import com.lz.common.core.domain.statistics.ro.StatisticsRo;
import com.lz.points.model.domain.PoStatisticsInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lz.points.model.dto.statistics.*;

/**
 * 统计信息Mapper接口
 *
 * @author YY
 * @date 2025-09-23
 */
public interface PoStatisticsInfoMapper extends BaseMapper<PoStatisticsInfo>
{
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
     * 删除统计信息
     *
     * @param statisticsId 统计信息主键
     * @return 结果
     */
    public int deletePoStatisticsInfoByStatisticsId(String statisticsId);

    /**
     * 批量删除统计信息
     *
     * @param statisticsIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePoStatisticsInfoByStatisticsIds(String[] statisticsIds);

    /**
     * 统计积分使用类型
     * @author: YY
     * @method: pointsUsageTypeStatistics
     * @date: 2025/9/23 16:50
     * @param request 查询参数
     * @return List<PointsUsageLogStatisticsRo>
     **/
    List<PointsUsageLogStatisticsRo> pointsUsageTypeStatistics(PointsUsageLogStatisticsRequest request);

    /**
     * 积分充值排行
     * @author: YY
     * @method: pointsOrderRank
     * @date: 2025/9/23 22:14
     * @param request 查询参数
     * @return List<PointsUsageLogStatisticsRo>
     **/
    List<PaymentOrderStatisticsRo> pointsOrderRankStatistics(PaymentOrderStatisticsRequest request);

    /**
     * 积分使用排行
     * @author: YY
     * @method: pointsUsageRank
     * @date: 2025/9/23 22:14
     * @param request 查询参数
     * @return List<PointsUsageLogStatisticsRo>
     **/
    List<StatisticsRo> pointsUsageStatistics(PointsUsageLogStatisticsRequest request);

    /**
     *
     * @author: YY
     * @method: pointsRechargeRankStatistics
     * @date: 2025/9/25 15:54
     * @param request 请求参数
     * @return List<PointsRechargeStatisticsRo>
     **/
    List<PointsRechargeStatisticsRo> pointsRechargePackageRankStatistics(PointsRechargeStatisticsRequest request);

    /**
     * 用户充值方式统计
     * @author: YY
     * @method: pointsPaymentStatistics
     * @date: 2025/9/25 16:56
     * @param request
     * @return List<PointsRechargeStatisticsRo>
     **/
    List<PointsRechargeStatisticsRo> pointsPaymentStatistics(PaymentOrderStatisticsRequest request);
}
