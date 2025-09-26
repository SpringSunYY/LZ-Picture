package com.lz.points.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lz.common.core.domain.statistics.vo.BarStatisticsVo;
import com.lz.common.core.domain.statistics.vo.LineStatisticsVo;
import com.lz.common.core.domain.statistics.vo.StatisticsVo;
import com.lz.points.model.domain.PoStatisticsInfo;
import com.lz.points.model.dto.poStatisticsInfo.PoStatisticsInfoQuery;
import com.lz.points.model.dto.statistics.PaymentOrderMapStatisticsRequest;
import com.lz.points.model.dto.statistics.PaymentOrderStatisticsRequest;
import com.lz.points.model.dto.statistics.PointsRechargeStatisticsRequest;
import com.lz.points.model.dto.statistics.PointsUsageLogStatisticsRequest;
import com.lz.points.model.vo.poStatisticsInfo.PoStatisticsInfoVo;
import com.lz.points.model.vo.statistics.PaymentOrderMapStatisticsVo;

import java.util.List;

/**
 * 统计信息Service接口
 *
 * @author YY
 * @date 2025-09-23
 */
public interface IPoStatisticsInfoService extends IService<PoStatisticsInfo> {
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

    /**
     * 根据日期和类型获取积分信息
     *
     * @param startDate 开始日期
     * @param end       结束日期
     * @param type      类型
     * @param commonKey 公共键
     */
    List<PoStatisticsInfo> getPoStatisticsInfosByDateAndKeyType(String startDate, String end, String type, String commonKey);

    /**
     * 积分使用类型统计
     *
     * @param request 请求参数
     * @return RadarStatisticsVo
     * @author: YY
     * @method: pointsUsageTypeStatistics
     * @date: 2025/9/23 16:46
     **/
    List<StatisticsVo> pointsUsageTypeStatistics(PointsUsageLogStatisticsRequest request);

    /**
     * 积分使用统计
     * @author: YY
     * @method: pointsUsageStatistics
     * @date: 2025/9/23 23:09
     * @param request 积分使用统计请求参数
     * @return LineStatisticsVo
     **/
    LineStatisticsVo pointsUsageStatistics(PointsUsageLogStatisticsRequest request);

    /**
     * 积分订单排行
     *
     * @param request 订单统计请求参数
     * @return BarStatisticsVo
     * @author: YY
     * @method: pointsOrderRank
     * @date: 2025/9/23 16:46
     **/
    BarStatisticsVo pointsOrderRankStatistics(PaymentOrderStatisticsRequest request);

    /**
     * 积分套餐充值排行
     * @author: YY
     * @method: pointsRechargeRankStatistics
     * @date: 2025/9/25 15:46
     * @param request
     * @return BarStatisticsVo
     **/
    BarStatisticsVo pointsRechargePackageRankStatistics(PointsRechargeStatisticsRequest request);

    /**
     * 用户支付方式统计
     * @author: YY
     * @method: pointsPaymentStatistics
     * @date: 2025/9/26 17:22
     * @param request 请求
     * @return List<StatisticsVo>
     **/
    List<StatisticsVo> pointsPaymentTypeStatistics(PaymentOrderStatisticsRequest request);

    /**
     * 积分订单以及金额区域
     * @author: YY
     * @method: pointsOrderIpAddressStatistics
     * @date: 2025/9/26 17:22
     * @param request 请求
     * @return List<PaymentOrderMapStatisticsVo>
     **/
    List<PaymentOrderMapStatisticsVo> pointsOrderIpAddressStatistics(PaymentOrderMapStatisticsRequest request);
}
