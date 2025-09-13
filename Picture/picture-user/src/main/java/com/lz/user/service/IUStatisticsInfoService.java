package com.lz.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lz.common.core.domain.statistics.vo.BarStatisticsVo;
import com.lz.common.core.domain.statistics.vo.LineStatisticsVo;
import com.lz.common.core.domain.statistics.vo.PieStatisticsVo;
import com.lz.common.core.domain.statistics.vo.RadarStatisticsVo;
import com.lz.user.model.domain.UStatisticsInfo;
import com.lz.user.model.dto.statistics.UserInformStatisticsRequest;
import com.lz.user.model.dto.statistics.UserLoginStatisticsRequest;
import com.lz.user.model.dto.statistics.UserStatisticsRequest;
import com.lz.user.model.dto.uStatisticsInfo.UStatisticsInfoQuery;
import com.lz.user.model.vo.statistics.UserInformStatisticsVo;
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

    /**
     * 用户性别统计
     *
     * @return
     */
    PieStatisticsVo userSexStatistics();

    /**
     * 获取通用统计信息，返回最新的
     *
     * @param
     * @return UStatisticsInfo
     * @author: YY
     * @method: getUStatisticsInfoByCommonKey
     * @date: 2025/9/11 20:33
     **/
    UStatisticsInfo getUStatisticsInfoByCommonKey(String commonKey, String type);

    /**
     * 用户年龄统计
     *
     * @return RadarStatisticsVo
     * @author: YY
     * @method: userAgeStatistics
     * @date: 2025/9/11 18:38
     **/
    RadarStatisticsVo userAgeStatistics();

    /**
     * 用户登录统计
     *
     * @param request
     * @return BarStatisticsVo
     * @author: YY
     * @method: userLoginStatistics
     * @date: 2025/9/12 15:50
     **/
    BarStatisticsVo userLoginStatistics(UserLoginStatisticsRequest request);

    /**
     * 根据时间范围、类型、键获取通用统计信息
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param type      类型
     * @param commonKey 键-公共
     * @return List<UStatisticsInfo>
     * @author: YY
     * @method: getUStatisticsInfosByDateAndKeyType
     * @date: 2025/9/12 16:01
     **/
    List<UStatisticsInfo> getUStatisticsInfosByDateAndKeyType(String startDate, String endDate, String type, String commonKey);

    /**
     * 用户消息发送统计
     *
     * @param request 统计请求
     * @return BarStatisticsVo
     * @author: YY
     * @method: userInformTypeStatistics
     * @date: 2025/9/12 18:36
     **/
    BarStatisticsVo userInformTypeStatistics(UserStatisticsRequest request);

    /**
     * 用户消息统计
     *
     * @param request 请求
     * @return List<UserInformStatisticsVo>
     * @author: YY
     * @method: userInformStatistics
     * @date: 2025/9/13 16:35
     **/
    List<UserInformStatisticsVo> userInformStatistics(UserInformStatisticsRequest request);

    /**
     * 用户总数统计
     *
     * @param
     * @return Long
     * @author: YY
     * @method: userTotalStatistics
     * @date: 2025/9/13 18:59
     **/
    Long userTotalStatistics();

    /**
     * 用户在线总数统计
     *
     * @param
     * @return Long
     * @author: YY
     * @method: userOnlineTotalStatistics
     * @date: 2025/9/13 19:14
     **/
    Long userOnlineTotalStatistics();
}
