package com.lz.ai.mapper;

import java.util.List;
import com.lz.ai.model.domain.UserUsageLogInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户AI使用记录Mapper接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface UserUsageLogInfoMapper extends BaseMapper<UserUsageLogInfo>
{
    /**
     * 查询用户AI使用记录
     *
     * @param logId 用户AI使用记录主键
     * @return 用户AI使用记录
     */
    public UserUsageLogInfo selectUserUsageLogInfoByLogId(String logId);

    /**
     * 查询用户AI使用记录列表
     *
     * @param userUsageLogInfo 用户AI使用记录
     * @return 用户AI使用记录集合
     */
    public List<UserUsageLogInfo> selectUserUsageLogInfoList(UserUsageLogInfo userUsageLogInfo);

    /**
     * 新增用户AI使用记录
     *
     * @param userUsageLogInfo 用户AI使用记录
     * @return 结果
     */
    public int insertUserUsageLogInfo(UserUsageLogInfo userUsageLogInfo);

    /**
     * 修改用户AI使用记录
     *
     * @param userUsageLogInfo 用户AI使用记录
     * @return 结果
     */
    public int updateUserUsageLogInfo(UserUsageLogInfo userUsageLogInfo);

    /**
     * 删除用户AI使用记录
     *
     * @param logId 用户AI使用记录主键
     * @return 结果
     */
    public int deleteUserUsageLogInfoByLogId(String logId);

    /**
     * 批量删除用户AI使用记录
     *
     * @param logIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserUsageLogInfoByLogIds(String[] logIds);
}
