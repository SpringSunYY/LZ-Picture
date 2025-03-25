package com.lz.points.service;

import java.util.List;
import com.lz.points.model.domain.PointsRechargePackageInfo;
import com.lz.points.model.vo.pointsRechargePackageInfo.PointsRechargePackageInfoVo;
import com.lz.points.model.dto.pointsRechargePackageInfo.PointsRechargePackageInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 充值积分套餐Service接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface IPointsRechargePackageInfoService extends IService<PointsRechargePackageInfo>
{
    //region mybatis代码
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
     * 批量删除充值积分套餐
     *
     * @param packageIds 需要删除的充值积分套餐主键集合
     * @return 结果
     */
    public int deletePointsRechargePackageInfoByPackageIds(String[] packageIds);

    /**
     * 删除充值积分套餐信息
     *
     * @param packageId 充值积分套餐主键
     * @return 结果
     */
    public int deletePointsRechargePackageInfoByPackageId(String packageId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param pointsRechargePackageInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PointsRechargePackageInfo> getQueryWrapper(PointsRechargePackageInfoQuery pointsRechargePackageInfoQuery);

    /**
     * 转换vo
     *
     * @param pointsRechargePackageInfoList PointsRechargePackageInfo集合
     * @return PointsRechargePackageInfoVO集合
     */
    List<PointsRechargePackageInfoVo> convertVoList(List<PointsRechargePackageInfo> pointsRechargePackageInfoList);
}
