package com.lz.picture.service;

import java.util.List;
import com.lz.picture.model.domain.UserViewLogInfo;
import com.lz.picture.model.vo.userViewLogInfo.UserViewLogInfoVo;
import com.lz.picture.model.dto.userViewLogInfo.UserViewLogInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 用户浏览记录Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface IUserViewLogInfoService extends IService<UserViewLogInfo>
{
    //region mybatis代码
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
     * 批量删除用户浏览记录
     *
     * @param viewIds 需要删除的用户浏览记录主键集合
     * @return 结果
     */
    public int deleteUserViewLogInfoByViewIds(String[] viewIds);

    /**
     * 删除用户浏览记录信息
     *
     * @param viewId 用户浏览记录主键
     * @return 结果
     */
    public int deleteUserViewLogInfoByViewId(String viewId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param userViewLogInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<UserViewLogInfo> getQueryWrapper(UserViewLogInfoQuery userViewLogInfoQuery);

    /**
     * 转换vo
     *
     * @param userViewLogInfoList UserViewLogInfo集合
     * @return UserViewLogInfoVO集合
     */
    List<UserViewLogInfoVo> convertVoList(List<UserViewLogInfo> userViewLogInfoList);
}
