package com.lz.user.service;

import java.util.List;
import com.lz.user.model.domain.LoginLogInfo;
import com.lz.user.model.vo.loginLogInfo.LoginLogInfoVo;
import com.lz.user.model.dto.loginLogInfo.LoginLogInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 用户登录日志Service接口
 *
 * @author YY
 * @date 2025-03-17
 */
public interface ILoginLogInfoService extends IService<LoginLogInfo>
{
    //region mybatis代码
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
     * 批量删除用户登录日志
     *
     * @param infoIds 需要删除的用户登录日志主键集合
     * @return 结果
     */
    public int deleteLoginLogInfoByInfoIds(String[] infoIds);

    /**
     * 删除用户登录日志信息
     *
     * @param infoId 用户登录日志主键
     * @return 结果
     */
    public int deleteLoginLogInfoByInfoId(String infoId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param loginLogInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<LoginLogInfo> getQueryWrapper(LoginLogInfoQuery loginLogInfoQuery);

    /**
     * 转换vo
     *
     * @param loginLogInfoList LoginLogInfo集合
     * @return LoginLogInfoVO集合
     */
    List<LoginLogInfoVo> convertVoList(List<LoginLogInfo> loginLogInfoList);
}
