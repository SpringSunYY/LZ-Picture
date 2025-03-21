package com.lz.user.service;

import java.util.List;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.model.vo.userInfo.UserInfoVo;
import com.lz.user.model.dto.userInfo.UserInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 用户信息Service接口
 *
 * @author YY
 * @date 2025-03-17
 */
public interface IUserInfoService extends IService<UserInfo>
{
    //region mybatis代码
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
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户信息主键集合
     * @return 结果
     */
    public int deleteUserInfoByUserIds(String[] userIds);

    /**
     * 删除用户信息信息
     *
     * @param userId 用户信息主键
     * @return 结果
     */
    public int deleteUserInfoByUserId(String userId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param userInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<UserInfo> getQueryWrapper(UserInfoQuery userInfoQuery);

    /**
     * 转换vo
     *
     * @param userInfoList UserInfo集合
     * @return UserInfoVO集合
     */
    List<UserInfoVo> convertVoList(List<UserInfo> userInfoList);
}
