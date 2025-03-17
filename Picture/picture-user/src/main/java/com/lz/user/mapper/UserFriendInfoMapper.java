package com.lz.user.mapper;

import java.util.List;
import com.lz.user.model.domain.UserFriendInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户好友关系Mapper接口
 *
 * @author YY
 * @date 2025-03-17
 */
public interface UserFriendInfoMapper extends BaseMapper<UserFriendInfo>
{
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
     * 删除用户好友关系
     *
     * @param relationId 用户好友关系主键
     * @return 结果
     */
    public int deleteUserFriendInfoByRelationId(String relationId);

    /**
     * 批量删除用户好友关系
     *
     * @param relationIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserFriendInfoByRelationIds(String[] relationIds);
}
