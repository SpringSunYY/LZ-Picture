package com.lz.points.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lz.points.model.domain.PointsRechargeInfo;
import com.lz.points.model.dto.pointsRechargeInfo.UserPointsRechargeInfoQuery;
import com.lz.points.model.vo.pointsRechargeInfo.PointsRechargeInfoVo;
import com.lz.points.model.dto.pointsRechargeInfo.PointsRechargeInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 积分充值记录Service接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface IPointsRechargeInfoService extends IService<PointsRechargeInfo> {
    //region mybatis代码

    /**
     * 查询积分充值记录
     *
     * @param rechargeId 积分充值记录主键
     * @return 积分充值记录
     */
    public PointsRechargeInfo selectPointsRechargeInfoByRechargeId(String rechargeId);

    /**
     * 查询积分充值记录列表
     *
     * @param pointsRechargeInfo 积分充值记录
     * @return 积分充值记录集合
     */
    public List<PointsRechargeInfo> selectPointsRechargeInfoList(PointsRechargeInfo pointsRechargeInfo);

    /**
     * 新增积分充值记录
     *
     * @param pointsRechargeInfo 积分充值记录
     * @return 结果
     */
    public int insertPointsRechargeInfo(PointsRechargeInfo pointsRechargeInfo);

    /**
     * 修改积分充值记录
     *
     * @param pointsRechargeInfo 积分充值记录
     * @return 结果
     */
    public int updatePointsRechargeInfo(PointsRechargeInfo pointsRechargeInfo);

    /**
     * 批量删除积分充值记录
     *
     * @param rechargeIds 需要删除的积分充值记录主键集合
     * @return 结果
     */
    public int deletePointsRechargeInfoByRechargeIds(String[] rechargeIds);

    /**
     * 删除积分充值记录信息
     *
     * @param rechargeId 积分充值记录主键
     * @return 结果
     */
    public int deletePointsRechargeInfoByRechargeId(String rechargeId);
    //endregion

    /**
     * 获取查询条件
     *
     * @param pointsRechargeInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PointsRechargeInfo> getQueryWrapper(PointsRechargeInfoQuery pointsRechargeInfoQuery);

    /**
     * 转换vo
     *
     * @param pointsRechargeInfoList PointsRechargeInfo集合
     * @return PointsRechargeInfoVO集合
     */
    List<PointsRechargeInfoVo> convertVoList(List<PointsRechargeInfo> pointsRechargeInfoList);

    /**
     * 自动更新过期订单
     *
     * @param expiredTime 过期时间
     */
    int autoUpdateExpiredOrder(Date expiredTime);

    /**
     * 用户查询自己的充值记录
     *
     * @param userPointsRechargeInfoQuery
     * @return Page<PointsRechargeInfo>
     * @author: YY
     * @method: selectMyPointsRechargeInfoList
     * @date: 2025/5/23 17:52
     **/
    Page<PointsRechargeInfo> selectMyPointsRechargeInfoList(UserPointsRechargeInfoQuery userPointsRechargeInfoQuery);
}
