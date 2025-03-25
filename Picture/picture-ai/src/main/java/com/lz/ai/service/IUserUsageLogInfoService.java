package com.lz.ai.service;

import java.util.List;
import com.lz.ai.model.domain.UserUsageLogInfo;
import com.lz.ai.model.vo.userUsageLogInfo.UserUsageLogInfoVo;
import com.lz.ai.model.dto.userUsageLogInfo.UserUsageLogInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 用户AI使用记录Service接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface IUserUsageLogInfoService extends IService<UserUsageLogInfo>
{
    //region mybatis代码
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
     * 批量删除用户AI使用记录
     *
     * @param logIds 需要删除的用户AI使用记录主键集合
     * @return 结果
     */
    public int deleteUserUsageLogInfoByLogIds(String[] logIds);

    /**
     * 删除用户AI使用记录信息
     *
     * @param logId 用户AI使用记录主键
     * @return 结果
     */
    public int deleteUserUsageLogInfoByLogId(String logId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param userUsageLogInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<UserUsageLogInfo> getQueryWrapper(UserUsageLogInfoQuery userUsageLogInfoQuery);

    /**
     * 转换vo
     *
     * @param userUsageLogInfoList UserUsageLogInfo集合
     * @return UserUsageLogInfoVO集合
     */
    List<UserUsageLogInfoVo> convertVoList(List<UserUsageLogInfo> userUsageLogInfoList);
}
