package com.lz.points.mapper;

import java.util.List;
import com.lz.points.model.domain.PointsRechargeInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 积分充值记录Mapper接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface PointsRechargeInfoMapper extends BaseMapper<PointsRechargeInfo>
{
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
     * 删除积分充值记录
     *
     * @param rechargeId 积分充值记录主键
     * @return 结果
     */
    public int deletePointsRechargeInfoByRechargeId(String rechargeId);

    /**
     * 批量删除积分充值记录
     *
     * @param rechargeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePointsRechargeInfoByRechargeIds(String[] rechargeIds);
}
