package com.lz.user.mapper;

import java.util.List;
import com.lz.user.model.domain.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户信息Mapper接口
 *
 * @author YY
 * @date 2025-03-17
 */
public interface UserInfoMapper extends BaseMapper<UserInfo>
{
    /**
     * 查询用户信息
     *
     * @param userId 用户信息主键
     * @return 用户信息
     */
    public UserInfo selectUserInfoByUserId(String userId);

    /**
     * 查询用户信息列表
     *
     * @param userInfo 用户信息
     * @return 用户信息集合
     */
    public List<UserInfo> selectUserInfoList(UserInfo userInfo);

    /**
     * 新增用户信息
     *
     * @param userInfo 用户信息
     * @return 结果
     */
    public int insertUserInfo(UserInfo userInfo);

    /**
     * 修改用户信息
     *
     * @param userInfo 用户信息
     * @return 结果
     */
    public int updateUserInfo(UserInfo userInfo);

    /**
     * 删除用户信息
     *
     * @param userId 用户信息主键
     * @return 结果
     */
    public int deleteUserInfoByUserId(String userId);

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserInfoByUserIds(String[] userIds);
}
