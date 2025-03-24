package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.SpaceMemberInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 空间成员信息Mapper接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface SpaceMemberInfoMapper extends BaseMapper<SpaceMemberInfo>
{
    /**
     * 查询空间成员信息
     *
     * @param memberId 空间成员信息主键
     * @return 空间成员信息
     */
    public SpaceMemberInfo selectSpaceMemberInfoByMemberId(String memberId);

    /**
     * 查询空间成员信息列表
     *
     * @param spaceMemberInfo 空间成员信息
     * @return 空间成员信息集合
     */
    public List<SpaceMemberInfo> selectSpaceMemberInfoList(SpaceMemberInfo spaceMemberInfo);

    /**
     * 新增空间成员信息
     *
     * @param spaceMemberInfo 空间成员信息
     * @return 结果
     */
    public int insertSpaceMemberInfo(SpaceMemberInfo spaceMemberInfo);

    /**
     * 修改空间成员信息
     *
     * @param spaceMemberInfo 空间成员信息
     * @return 结果
     */
    public int updateSpaceMemberInfo(SpaceMemberInfo spaceMemberInfo);

    /**
     * 删除空间成员信息
     *
     * @param memberId 空间成员信息主键
     * @return 结果
     */
    public int deleteSpaceMemberInfoByMemberId(String memberId);

    /**
     * 批量删除空间成员信息
     *
     * @param memberIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSpaceMemberInfoByMemberIds(String[] memberIds);
}
