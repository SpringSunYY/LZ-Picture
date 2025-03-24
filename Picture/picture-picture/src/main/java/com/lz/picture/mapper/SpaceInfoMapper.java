package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.SpaceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 空间信息Mapper接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface SpaceInfoMapper extends BaseMapper<SpaceInfo>
{
    /**
     * 查询空间信息
     *
     * @param spaceId 空间信息主键
     * @return 空间信息
     */
    public SpaceInfo selectSpaceInfoBySpaceId(String spaceId);

    /**
     * 查询空间信息列表
     *
     * @param spaceInfo 空间信息
     * @return 空间信息集合
     */
    public List<SpaceInfo> selectSpaceInfoList(SpaceInfo spaceInfo);

    /**
     * 新增空间信息
     *
     * @param spaceInfo 空间信息
     * @return 结果
     */
    public int insertSpaceInfo(SpaceInfo spaceInfo);

    /**
     * 修改空间信息
     *
     * @param spaceInfo 空间信息
     * @return 结果
     */
    public int updateSpaceInfo(SpaceInfo spaceInfo);

    /**
     * 删除空间信息
     *
     * @param spaceId 空间信息主键
     * @return 结果
     */
    public int deleteSpaceInfoBySpaceId(String spaceId);

    /**
     * 批量删除空间信息
     *
     * @param spaceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSpaceInfoBySpaceIds(String[] spaceIds);
}
