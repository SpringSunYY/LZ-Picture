package com.lz.picture.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lz.common.core.page.TableDataInfo;
import com.lz.picture.model.domain.SpaceMemberInfo;
import com.lz.picture.model.dto.spaceMemberInfo.SpaceMemberInfoQuery;
import com.lz.picture.model.dto.spaceMemberInfo.UserSpaceMemberInfoQuery;
import com.lz.picture.model.vo.spaceMemberInfo.SpaceMemberInfoVo;

import java.util.List;

/**
 * 空间成员信息Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface ISpaceMemberInfoService extends IService<SpaceMemberInfo> {
    //region mybatis代码

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
     * 批量删除空间成员信息
     *
     * @param memberIds 需要删除的空间成员信息主键集合
     * @return 结果
     */
    public int deleteSpaceMemberInfoByMemberIds(String[] memberIds);

    /**
     * 删除空间成员信息信息
     *
     * @param memberId 空间成员信息主键
     * @return 结果
     */
    public int deleteSpaceMemberInfoByMemberId(String memberId);
    //endregion

    /**
     * 获取查询条件
     *
     * @param spaceMemberInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<SpaceMemberInfo> getQueryWrapper(SpaceMemberInfoQuery spaceMemberInfoQuery);

    /**
     * 转换vo
     *
     * @param spaceMemberInfoList SpaceMemberInfo集合
     * @return SpaceMemberInfoVO集合
     */
    List<SpaceMemberInfoVo> convertVoList(List<SpaceMemberInfo> spaceMemberInfoList);

    /**
     * 获取空间人数
     *
     * @param spaceId
     * @return Long
     * @author: YY
     * @method: getSpaceMemberNumberCount
     * @date: 2025/6/29 18:54
     **/
    Long getSpaceMemberNumberCount(String spaceId);

    /**
     * 获取用户空间成员列表
     *
     * @param query
     * @return
     */
    TableDataInfo listUserSpaceMemberInfoList(UserSpaceMemberInfoQuery query);

    //删除空间成员缓存
    void deleteSpaceMemberCacheBySpaceId(String spaceId);

    int userDeleteSpaceMemberInfoByMemberId(String memberId);
}
