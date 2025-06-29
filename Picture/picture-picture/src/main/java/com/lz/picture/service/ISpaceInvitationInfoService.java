package com.lz.picture.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lz.common.core.page.TableDataInfo;
import com.lz.picture.model.domain.SpaceInvitationInfo;
import com.lz.picture.model.dto.spaceInvitationInfo.SpaceInvitationInfoQuery;
import com.lz.picture.model.dto.spaceInvitationInfo.UserSpaceInvitationInfoQuery;
import com.lz.picture.model.vo.spaceInvitationInfo.SpaceInvitationInfoVo;

import java.util.List;

/**
 * 空间成员邀请记录Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface ISpaceInvitationInfoService extends IService<SpaceInvitationInfo> {
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

    /**
     * 用户邀请其他用户创建空间
     *
     * @param spaceInvitationInfo
     * @return int
     * @author: YY
     * @method: userInsertSpaceInvitationInfo
     * @date: 2025/6/29 19:31
     **/
    int userInsertSpaceInvitationInfo(SpaceInvitationInfo spaceInvitationInfo);

    /**
     * 查询用户邀请信息
     *
     * @param userSpaceInvitationInfoQuery
     * @return TableDataInfo
     * @author: YY
     * @method: listUserSpaceInvitationInfoTable
     * @date: 2025/6/29 20:29
     **/
    TableDataInfo listUserSpaceInvitationInfoTable(UserSpaceInvitationInfoQuery userSpaceInvitationInfoQuery);
}
