package com.lz.user.mapper;

import java.util.List;
import com.lz.user.model.domain.LoginLogInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户登录日志Mapper接口
 *
 * @author YY
 * @date 2025-03-17
 */
public interface LoginLogInfoMapper extends BaseMapper<LoginLogInfo>
{
    /**
     * 查询用户登录日志
     *
     * @param infoId 用户登录日志主键
     * @return 用户登录日志
     */
    public LoginLogInfo selectLoginLogInfoByInfoId(String infoId);

    /**
     * 查询用户登录日志列表
     *
     * @param loginLogInfo 用户登录日志
     * @return 用户登录日志集合
     */
    public List<LoginLogInfo> selectLoginLogInfoList(LoginLogInfo loginLogInfo);

    /**
     * 新增用户登录日志
     *
     * @param loginLogInfo 用户登录日志
     * @return 结果
     */
    public int insertLoginLogInfo(LoginLogInfo loginLogInfo);

    /**
     * 修改用户登录日志
     *
     * @param loginLogInfo 用户登录日志
     * @return 结果
     */
    public int updateLoginLogInfo(LoginLogInfo loginLogInfo);

    /**
     * 删除用户登录日志
     *
     * @param infoId 用户登录日志主键
     * @return 结果
     */
    public int deleteLoginLogInfoByInfoId(String infoId);

    /**
     * 批量删除用户登录日志
     *
     * @param infoIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLoginLogInfoByInfoIds(String[] infoIds);
}
