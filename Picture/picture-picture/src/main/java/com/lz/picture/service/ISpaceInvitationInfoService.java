package com.lz.picture.service;

import java.util.List;
import com.lz.picture.model.domain.SpaceInvitationInfo;
import com.lz.picture.model.vo.spaceInvitationInfo.SpaceInvitationInfoVo;
import com.lz.picture.model.dto.spaceInvitationInfo.SpaceInvitationInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 空间成员邀请记录Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface ISpaceInvitationInfoService extends IService<SpaceInvitationInfo>
{
    //region mybatis代码
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
     * 批量删除空间成员邀请记录
     *
     * @param invitationIds 需要删除的空间成员邀请记录主键集合
     * @return 结果
     */
    public int deleteSpaceInvitationInfoByInvitationIds(String[] invitationIds);

    /**
     * 删除空间成员邀请记录信息
     *
     * @param invitationId 空间成员邀请记录主键
     * @return 结果
     */
    public int deleteSpaceInvitationInfoByInvitationId(String invitationId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param spaceInvitationInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<SpaceInvitationInfo> getQueryWrapper(SpaceInvitationInfoQuery spaceInvitationInfoQuery);

    /**
     * 转换vo
     *
     * @param spaceInvitationInfoList SpaceInvitationInfo集合
     * @return SpaceInvitationInfoVO集合
     */
    List<SpaceInvitationInfoVo> convertVoList(List<SpaceInvitationInfo> spaceInvitationInfoList);
}
