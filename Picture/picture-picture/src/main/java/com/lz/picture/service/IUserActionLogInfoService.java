package com.lz.picture.service;

import java.util.List;
import com.lz.picture.model.domain.UserActionLogInfo;
import com.lz.picture.model.vo.userActionLogInfo.UserActionLogInfoVo;
import com.lz.picture.model.dto.userActionLogInfo.UserActionLogInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 用户行为日志Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface IUserActionLogInfoService extends IService<UserActionLogInfo>
{
    //region mybatis代码
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
     * 批量删除用户行为日志
     *
     * @param actionIds 需要删除的用户行为日志主键集合
     * @return 结果
     */
    public int deleteUserActionLogInfoByActionIds(String[] actionIds);

    /**
     * 删除用户行为日志信息
     *
     * @param actionId 用户行为日志主键
     * @return 结果
     */
    public int deleteUserActionLogInfoByActionId(String actionId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param userActionLogInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<UserActionLogInfo> getQueryWrapper(UserActionLogInfoQuery userActionLogInfoQuery);

    /**
     * 转换vo
     *
     * @param userActionLogInfoList UserActionLogInfo集合
     * @return UserActionLogInfoVO集合
     */
    List<UserActionLogInfoVo> convertVoList(List<UserActionLogInfo> userActionLogInfoList);
}
