package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.UserActionLogInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户行为日志Mapper接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface UserActionLogInfoMapper extends BaseMapper<UserActionLogInfo>
{
    /**
     * 查询用户行为日志
     *
     * @param actionId 用户行为日志主键
     * @return 用户行为日志
     */
    public UserActionLogInfo selectUserActionLogInfoByActionId(String actionId);

    /**
     * 查询用户行为日志列表
     *
     * @param userActionLogInfo 用户行为日志
     * @return 用户行为日志集合
     */
    public List<UserActionLogInfo> selectUserActionLogInfoList(UserActionLogInfo userActionLogInfo);

    /**
     * 新增用户行为日志
     *
     * @param userActionLogInfo 用户行为日志
     * @return 结果
     */
    public int insertUserActionLogInfo(UserActionLogInfo userActionLogInfo);

    /**
     * 修改用户行为日志
     *
     * @param userActionLogInfo 用户行为日志
     * @return 结果
     */
    public int updateUserActionLogInfo(UserActionLogInfo userActionLogInfo);

    /**
     * 删除用户行为日志
     *
     * @param actionId 用户行为日志主键
     * @return 结果
     */
    public int deleteUserActionLogInfoByActionId(String actionId);

    /**
     * 批量删除用户行为日志
     *
     * @param actionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserActionLogInfoByActionIds(String[] actionIds);
}
