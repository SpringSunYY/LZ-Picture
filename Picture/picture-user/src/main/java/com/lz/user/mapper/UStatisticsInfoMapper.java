package com.lz.user.mapper;

import java.util.List;

import com.lz.common.core.domain.statistics.ro.MapStatisticsRo;
import com.lz.common.core.domain.statistics.ro.StatisticsRo;
import com.lz.user.model.domain.UStatisticsInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lz.user.model.dto.statistics.UserInformTypeStatisticsRo;
import com.lz.user.model.dto.statistics.UserLoginStatisticsRequest;
import com.lz.user.model.dto.statistics.UserStatisticsRequest;
import org.apache.ibatis.annotations.Param;

/**
 * 统计信息Mapper接口
 *
 * @author YY
 * @date 2025-09-09
 */
public interface UStatisticsInfoMapper extends BaseMapper<UStatisticsInfo>
{
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
     * 删除统计信息
     *
     * @param statisticsId 统计信息主键
     * @return 结果
     */
    public int deleteUStatisticsInfoByStatisticsId(String statisticsId);

    /**
     * 批量删除统计信息
     *
     * @param statisticsIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUStatisticsInfoByStatisticsIds(String[] statisticsIds);

    /**
     * 用户注册统计
     * @param request 请求
     * @return
     */
    List<StatisticsRo> userRegisterStatistics(UserStatisticsRequest request);

    /**
     * 用户登录统计
     *
     * @param request 请求
     * @return
     */
    List<StatisticsRo> userLoginStatistics(UserLoginStatisticsRequest request);

    /**
     * 用户性别统计
     * @return
     */
    List<StatisticsRo> userSexStatistics(@Param("isDelete") String isDelete);

    /**
     * 用户信息统计
     * @return
     */
    List<UserInformTypeStatisticsRo> userInformTypeStatistics(UserStatisticsRequest request);

    List<MapStatisticsRo> userLocationStatistics(UserStatisticsRequest request);
}
