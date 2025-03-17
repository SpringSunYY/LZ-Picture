package com.lz.user.service;

import java.util.List;
import com.lz.user.model.domain.UserFriendInfo;
import com.lz.user.model.vo.userFriendInfo.UserFriendInfoVo;
import com.lz.user.model.dto.userFriendInfo.UserFriendInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 用户好友关系Service接口
 *
 * @author YY
 * @date 2025-03-17
 */
public interface IUserFriendInfoService extends IService<UserFriendInfo>
{
    //region mybatis代码
    /**
     * 查询用户好友关系
     *
     * @param relationId 用户好友关系主键
     * @return 用户好友关系
     */
    public UserFriendInfo selectUserFriendInfoByRelationId(String relationId);

    /**
     * 查询用户好友关系列表
     *
     * @param userFriendInfo 用户好友关系
     * @return 用户好友关系集合
     */
    public List<UserFriendInfo> selectUserFriendInfoList(UserFriendInfo userFriendInfo);

    /**
     * 新增用户好友关系
     *
     * @param userFriendInfo 用户好友关系
     * @return 结果
     */
    public int insertUserFriendInfo(UserFriendInfo userFriendInfo);

    /**
     * 修改用户好友关系
     *
     * @param userFriendInfo 用户好友关系
     * @return 结果
     */
    public int updateUserFriendInfo(UserFriendInfo userFriendInfo);

    /**
     * 批量删除用户好友关系
     *
     * @param relationIds 需要删除的用户好友关系主键集合
     * @return 结果
     */
    public int deleteUserFriendInfoByRelationIds(String[] relationIds);

    /**
     * 删除用户好友关系信息
     *
     * @param relationId 用户好友关系主键
     * @return 结果
     */
    public int deleteUserFriendInfoByRelationId(String relationId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param userFriendInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<UserFriendInfo> getQueryWrapper(UserFriendInfoQuery userFriendInfoQuery);

    /**
     * 转换vo
     *
     * @param userFriendInfoList UserFriendInfo集合
     * @return UserFriendInfoVO集合
     */
    List<UserFriendInfoVo> convertVoList(List<UserFriendInfo> userFriendInfoList);
}
