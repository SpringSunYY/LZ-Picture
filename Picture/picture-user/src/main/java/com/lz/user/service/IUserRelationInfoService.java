package com.lz.user.service;

import java.util.List;
import com.lz.user.model.domain.UserRelationInfo;
import com.lz.user.model.vo.userRelationInfo.UserRelationInfoVo;
import com.lz.user.model.dto.userRelationInfo.UserRelationInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 用户关系Service接口
 *
 * @author YY
 * @date 2025-03-17
 */
public interface IUserRelationInfoService extends IService<UserRelationInfo>
{
    //region mybatis代码
    /**
     * 查询用户关系
     *
     * @param relationId 用户关系主键
     * @return 用户关系
     */
    public UserRelationInfo selectUserRelationInfoByRelationId(String relationId);

    /**
     * 查询用户关系列表
     *
     * @param userRelationInfo 用户关系
     * @return 用户关系集合
     */
    public List<UserRelationInfo> selectUserRelationInfoList(UserRelationInfo userRelationInfo);

    /**
     * 新增用户关系
     *
     * @param userRelationInfo 用户关系
     * @return 结果
     */
    public int insertUserRelationInfo(UserRelationInfo userRelationInfo);

    /**
     * 修改用户关系
     *
     * @param userRelationInfo 用户关系
     * @return 结果
     */
    public int updateUserRelationInfo(UserRelationInfo userRelationInfo);

    /**
     * 批量删除用户关系
     *
     * @param relationIds 需要删除的用户关系主键集合
     * @return 结果
     */
    public int deleteUserRelationInfoByRelationIds(String[] relationIds);

    /**
     * 删除用户关系信息
     *
     * @param relationId 用户关系主键
     * @return 结果
     */
    public int deleteUserRelationInfoByRelationId(String relationId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param userRelationInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<UserRelationInfo> getQueryWrapper(UserRelationInfoQuery userRelationInfoQuery);

    /**
     * 转换vo
     *
     * @param userRelationInfoList UserRelationInfo集合
     * @return UserRelationInfoVO集合
     */
    List<UserRelationInfoVo> convertVoList(List<UserRelationInfo> userRelationInfoList);
}
