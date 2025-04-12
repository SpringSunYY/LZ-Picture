package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.UserViewLogInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户浏览记录Mapper接口
 *
 * @author YY
 * @date 2025-04-12
 */
public interface UserViewLogInfoMapper extends BaseMapper<UserViewLogInfo>
{
    /**
     * 查询用户浏览记录
     *
     * @param viewId 用户浏览记录主键
     * @return 用户浏览记录
     */
    public UserViewLogInfo selectUserViewLogInfoByViewId(String viewId);

    /**
     * 查询用户浏览记录列表
     *
     * @param userViewLogInfo 用户浏览记录
     * @return 用户浏览记录集合
     */
    public List<UserViewLogInfo> selectUserViewLogInfoList(UserViewLogInfo userViewLogInfo);

    /**
     * 新增用户浏览记录
     *
     * @param userViewLogInfo 用户浏览记录
     * @return 结果
     */
    public int insertUserViewLogInfo(UserViewLogInfo userViewLogInfo);

    /**
     * 修改用户浏览记录
     *
     * @param userViewLogInfo 用户浏览记录
     * @return 结果
     */
    public int updateUserViewLogInfo(UserViewLogInfo userViewLogInfo);

    /**
     * 删除用户浏览记录
     *
     * @param viewId 用户浏览记录主键
     * @return 结果
     */
    public int deleteUserViewLogInfoByViewId(String viewId);

    /**
     * 批量删除用户浏览记录
     *
     * @param viewIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserViewLogInfoByViewIds(String[] viewIds);
}
