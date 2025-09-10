package com.lz.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lz.common.core.domain.statistics.vo.LineStatisticsVo;
import com.lz.user.model.domain.UStatisticsInfo;
import com.lz.user.model.dto.statistics.UserStatisticsRequest;
import com.lz.user.model.dto.uStatisticsInfo.UStatisticsInfoQuery;
import com.lz.user.model.vo.uStatisticsInfo.UStatisticsInfoVo;

import java.util.List;

/**
 * 统计信息Service接口
 *
 * @author YY
 * @date 2025-09-09
 */
public interface IUStatisticsInfoService extends IService<UStatisticsInfo> {
    //region mybatis代码

    /**
     * 查询统计信息
     *
     * @param statisticsId 统计信息主键
     * @return 统计信息
     */
    public UStatisticsInfo selectUStatisticsInfoByStatisticsId(String statisticsId);

    /**
     * 查询统计信息列表
     *
     * @param uStatisticsInfo 统计信息
     * @return 统计信息集合
     */
    public List<UStatisticsInfo> selectUStatisticsInfoList(UStatisticsInfo uStatisticsInfo);

    /**
     * 新增统计信息
     *
     * @param uStatisticsInfo 统计信息
     * @return 结果
     */
    public int insertUStatisticsInfo(UStatisticsInfo uStatisticsInfo);

    /**
     * 修改统计信息
     *
     * @param uStatisticsInfo 统计信息
     * @return 结果
     */
    public int updateUStatisticsInfo(UStatisticsInfo uStatisticsInfo);

    /**
     * 批量删除统计信息
     *
     * @param statisticsIds 需要删除的统计信息主键集合
     * @return 结果
     */
    public int deleteUStatisticsInfoByStatisticsIds(String[] statisticsIds);

    /**
     * 删除统计信息信息
     *
     * @param statisticsId 统计信息主键
     * @return 结果
     */
    public int deleteUStatisticsInfoByStatisticsId(String statisticsId);
    //endregion

    /**
     * 获取查询条件
     *
     * @param uStatisticsInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<UStatisticsInfo> getQueryWrapper(UStatisticsInfoQuery uStatisticsInfoQuery);

    /**
     * 转换vo
     *
     * @param uStatisticsInfoList UStatisticsInfo集合
     * @return UStatisticsInfoVO集合
     */
    List<UStatisticsInfoVo> convertVoList(List<UStatisticsInfo> uStatisticsInfoList);

    /**
     * 用户注册统计
     *
     * @param userStatisticsRequest 统计请求
     * @return LineStatisticsVo
     * @author: YY
     * @method: userRegisterStatistics
     * @date: 2025/9/10 15:53
     **/
    LineStatisticsVo userRegisterStatistics(UserStatisticsRequest userStatisticsRequest);
}
