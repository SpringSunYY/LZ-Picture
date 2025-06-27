package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.SpaceDilatationInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 空间扩容信息Mapper接口
 *
 * @author YY
 * @date 2025-06-28
 */
public interface SpaceDilatationInfoMapper extends BaseMapper<SpaceDilatationInfo>
{
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
     * 删除空间扩容信息
     *
     * @param dilatationId 空间扩容信息主键
     * @return 结果
     */
    public int deleteSpaceDilatationInfoByDilatationId(String dilatationId);

    /**
     * 批量删除空间扩容信息
     *
     * @param dilatationIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSpaceDilatationInfoByDilatationIds(String[] dilatationIds);
}
