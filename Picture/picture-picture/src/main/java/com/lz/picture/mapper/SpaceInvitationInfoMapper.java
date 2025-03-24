package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.SpaceInvitationInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 空间成员邀请记录Mapper接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface SpaceInvitationInfoMapper extends BaseMapper<SpaceInvitationInfo>
{
    /**
     * 查询空间成员邀请记录
     *
     * @param invitationId 空间成员邀请记录主键
     * @return 空间成员邀请记录
     */
    public SpaceInvitationInfo selectSpaceInvitationInfoByInvitationId(String invitationId);

    /**
     * 查询空间成员邀请记录列表
     *
     * @param spaceInvitationInfo 空间成员邀请记录
     * @return 空间成员邀请记录集合
     */
    public List<SpaceInvitationInfo> selectSpaceInvitationInfoList(SpaceInvitationInfo spaceInvitationInfo);

    /**
     * 新增空间成员邀请记录
     *
     * @param spaceInvitationInfo 空间成员邀请记录
     * @return 结果
     */
    public int insertSpaceInvitationInfo(SpaceInvitationInfo spaceInvitationInfo);

    /**
     * 修改空间成员邀请记录
     *
     * @param spaceInvitationInfo 空间成员邀请记录
     * @return 结果
     */
    public int updateSpaceInvitationInfo(SpaceInvitationInfo spaceInvitationInfo);

    /**
     * 删除空间成员邀请记录
     *
     * @param invitationId 空间成员邀请记录主键
     * @return 结果
     */
    public int deleteSpaceInvitationInfoByInvitationId(String invitationId);

    /**
     * 批量删除空间成员邀请记录
     *
     * @param invitationIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSpaceInvitationInfoByInvitationIds(String[] invitationIds);
}
