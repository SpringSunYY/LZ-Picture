package com.lz.points.mapper;

import java.util.List;
import com.lz.points.model.domain.PointsRechargePackageInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 充值积分套餐Mapper接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface PointsRechargePackageInfoMapper extends BaseMapper<PointsRechargePackageInfo>
{
    /**
     * 查询充值积分套餐
     *
     * @param packageId 充值积分套餐主键
     * @return 充值积分套餐
     */
    public PointsRechargePackageInfo selectPointsRechargePackageInfoByPackageId(String packageId);

    /**
     * 查询充值积分套餐列表
     *
     * @param pointsRechargePackageInfo 充值积分套餐
     * @return 充值积分套餐集合
     */
    public List<PointsRechargePackageInfo> selectPointsRechargePackageInfoList(PointsRechargePackageInfo pointsRechargePackageInfo);

    /**
     * 新增充值积分套餐
     *
     * @param pointsRechargePackageInfo 充值积分套餐
     * @return 结果
     */
    public int insertPointsRechargePackageInfo(PointsRechargePackageInfo pointsRechargePackageInfo);

    /**
     * 修改充值积分套餐
     *
     * @param pointsRechargePackageInfo 充值积分套餐
     * @return 结果
     */
    public int updatePointsRechargePackageInfo(PointsRechargePackageInfo pointsRechargePackageInfo);

    /**
     * 删除充值积分套餐
     *
     * @param packageId 充值积分套餐主键
     * @return 结果
     */
    public int deletePointsRechargePackageInfoByPackageId(String packageId);

    /**
     * 批量删除充值积分套餐
     *
     * @param packageIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePointsRechargePackageInfoByPackageIds(String[] packageIds);
}
