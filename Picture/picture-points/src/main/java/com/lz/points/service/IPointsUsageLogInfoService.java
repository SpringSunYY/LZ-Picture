package com.lz.points.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lz.points.model.domain.PointsRechargeInfo;
import com.lz.points.model.domain.PointsUsageLogInfo;
import com.lz.points.model.dto.pointsUsageLogInfo.UserPointsUsageLogInfoQuery;
import com.lz.points.model.vo.pointsUsageLogInfo.PointsUsageLogInfoVo;
import com.lz.points.model.dto.pointsUsageLogInfo.PointsUsageLogInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 积分使用记录Service接口
 *
 * @author YY
 * @date 2025-05-23
 */
public interface IPointsUsageLogInfoService extends IService<PointsUsageLogInfo> {
    //region mybatis代码

    /**
     * 查询积分使用记录
     *
     * @param logId 积分使用记录主键
     * @return 积分使用记录
     */
    public PointsUsageLogInfo selectPointsUsageLogInfoByLogId(String logId);

    /**
     * 查询积分使用记录列表
     *
     * @param pointsUsageLogInfo 积分使用记录
     * @return 积分使用记录集合
     */
    public List<PointsUsageLogInfo> selectPointsUsageLogInfoList(PointsUsageLogInfo pointsUsageLogInfo);

    /**
     * 新增积分使用记录
     *
     * @param pointsUsageLogInfo 积分使用记录
     * @return 结果
     */
    public int insertPointsUsageLogInfo(PointsUsageLogInfo pointsUsageLogInfo);

    /**
     * 修改积分使用记录
     *
     * @param pointsUsageLogInfo 积分使用记录
     * @return 结果
     */
    public int updatePointsUsageLogInfo(PointsUsageLogInfo pointsUsageLogInfo);

    /**
     * 批量删除积分使用记录
     *
     * @param logIds 需要删除的积分使用记录主键集合
     * @return 结果
     */
    public int deletePointsUsageLogInfoByLogIds(String[] logIds);

    /**
     * 删除积分使用记录信息
     *
     * @param logId 积分使用记录主键
     * @return 结果
     */
    public int deletePointsUsageLogInfoByLogId(String logId);
    //endregion

    /**
     * 获取查询条件
     *
     * @param pointsUsageLogInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PointsUsageLogInfo> getQueryWrapper(PointsUsageLogInfoQuery pointsUsageLogInfoQuery);

    /**
     * 转换vo
     *
     * @param pointsUsageLogInfoList PointsUsageLogInfo集合
     * @return PointsUsageLogInfoVO集合
     */
    List<PointsUsageLogInfoVo> convertVoList(List<PointsUsageLogInfo> pointsUsageLogInfoList);

    /**
     * 用户查询积分使用记录列表
     *
     * @param userPointsUsageLogInfoQuery 查询条件对象
     * @return Page<PointsUsageLogInfo>
     * @author: YY
     * @method: selectMyPointsUsageLogInfoList
     * @date: 2025/5/24 00:25
     **/
    Page<PointsUsageLogInfo> selectMyPointsUsageLogInfoList(UserPointsUsageLogInfoQuery userPointsUsageLogInfoQuery);
}
