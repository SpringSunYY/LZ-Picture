package com.lz.user.mapper;

import java.util.List;
import com.lz.user.model.domain.UserRelationInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户关系Mapper接口
 *
 * @author YY
 * @date 2025-03-17
 */
public interface UserRelationInfoMapper extends BaseMapper<UserRelationInfo>
{
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
     * 删除用户关系
     *
     * @param relationId 用户关系主键
     * @return 结果
     */
    public int deleteUserRelationInfoByRelationId(String relationId);

    /**
     * 批量删除用户关系
     *
     * @param relationIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserRelationInfoByRelationIds(String[] relationIds);
}
