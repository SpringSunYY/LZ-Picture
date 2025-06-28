package com.lz.picture.service;

import java.util.List;
import com.lz.picture.model.domain.SpaceDilatationInfo;
import com.lz.picture.model.vo.spaceDilatationInfo.SpaceDilatationInfoCalculationVo;
import com.lz.picture.model.vo.spaceDilatationInfo.SpaceDilatationInfoVo;
import com.lz.picture.model.dto.spaceDilatationInfo.SpaceDilatationInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 空间扩容信息Service接口
 *
 * @author YY
 * @date 2025-06-28
 */
public interface ISpaceDilatationInfoService extends IService<SpaceDilatationInfo>
{
    //region mybatis代码
    /**
     * 查询空间扩容信息
     *
     * @param dilatationId 空间扩容信息主键
     * @return 空间扩容信息
     */
    public SpaceDilatationInfo selectSpaceDilatationInfoByDilatationId(String dilatationId);

    /**
     * 查询空间扩容信息列表
     *
     * @param spaceDilatationInfo 空间扩容信息
     * @return 空间扩容信息集合
     */
    public List<SpaceDilatationInfo> selectSpaceDilatationInfoList(SpaceDilatationInfo spaceDilatationInfo);

    /**
     * 新增空间扩容信息
     *
     * @param spaceDilatationInfo 空间扩容信息
     * @return 结果
     */
    public int insertSpaceDilatationInfo(SpaceDilatationInfo spaceDilatationInfo);

    /**
     * 修改空间扩容信息
     *
     * @param spaceDilatationInfo 空间扩容信息
     * @return 结果
     */
    public int updateSpaceDilatationInfo(SpaceDilatationInfo spaceDilatationInfo);

    /**
     * 批量删除空间扩容信息
     *
     * @param dilatationIds 需要删除的空间扩容信息主键集合
     * @return 结果
     */
    public int deleteSpaceDilatationInfoByDilatationIds(String[] dilatationIds);

    /**
     * 删除空间扩容信息信息
     *
     * @param dilatationId 空间扩容信息主键
     * @return 结果
     */
    public int deleteSpaceDilatationInfoByDilatationId(String dilatationId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param spaceDilatationInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<SpaceDilatationInfo> getQueryWrapper(SpaceDilatationInfoQuery spaceDilatationInfoQuery);

    /**
     * 转换vo
     *
     * @param spaceDilatationInfoList SpaceDilatationInfo集合
     * @return SpaceDilatationInfoVO集合
     */
    List<SpaceDilatationInfoVo> convertVoList(List<SpaceDilatationInfo> spaceDilatationInfoList);

    /**
     * 用户天剑空间扩容信息
     * @author: YY
     * @method: userInsertSpaceDilatationInfo
     * @date: 2025/6/28 18:53
     * @param spaceDilatationInfo 扩容信息
     * @return int
     **/
    int userInsertSpaceDilatationInfo(SpaceDilatationInfo spaceDilatationInfo);

    /**
     * 计算扩容所需要总积分
     * @author: YY
     * @method: calculateDilatationPointTotal
     * @date: 2025/6/28 19:24
     * @param dilatationTotal 扩容总数
     * @param dilatationType 扩容类型
     * @return SpaceDilatationInfoCalculationVo
     **/
    SpaceDilatationInfoCalculationVo calculateDilatationPointTotal(Long dilatationTotal, String dilatationType);
}
