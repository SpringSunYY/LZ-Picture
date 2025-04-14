package com.lz.picture.service;

import java.util.List;

import com.lz.picture.model.domain.UserBehaviorInfo;
import com.lz.picture.model.vo.userBehaviorInfo.UserBehaviorInfoVo;
import com.lz.picture.model.dto.userBehaviorInfo.UserBehaviorInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 用户行为Service接口
 *
 * @author YY
 * @date 2025-04-12
 */
public interface IUserBehaviorInfoService extends IService<UserBehaviorInfo> {
    //region mybatis代码

    /**
     * 查询用户行为
     *
     * @param behaviorId 用户行为主键
     * @return 用户行为
     */
    public UserBehaviorInfo selectUserBehaviorInfoByBehaviorId(String behaviorId);

    /**
     * 查询用户行为列表
     *
     * @param userBehaviorInfo 用户行为
     * @return 用户行为集合
     */
    public List<UserBehaviorInfo> selectUserBehaviorInfoList(UserBehaviorInfo userBehaviorInfo);

    /**
     * 新增用户行为
     *
     * @param userBehaviorInfo 用户行为
     * @return 结果
     */
    public int insertUserBehaviorInfo(UserBehaviorInfo userBehaviorInfo);

    /**
     * 修改用户行为
     *
     * @param userBehaviorInfo 用户行为
     * @return 结果
     */
    public int updateUserBehaviorInfo(UserBehaviorInfo userBehaviorInfo);

    /**
     * 批量删除用户行为
     *
     * @param behaviorIds 需要删除的用户行为主键集合
     * @return 结果
     */
    public int deleteUserBehaviorInfoByBehaviorIds(String[] behaviorIds);

    /**
     * 删除用户行为信息
     *
     * @param behaviorId 用户行为主键
     * @return 结果
     */
    public int deleteUserBehaviorInfoByBehaviorId(String behaviorId);
    //endregion

    /**
     * 获取查询条件
     *
     * @param userBehaviorInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<UserBehaviorInfo> getQueryWrapper(UserBehaviorInfoQuery userBehaviorInfoQuery);

    /**
     * 转换vo
     *
     * @param userBehaviorInfoList UserBehaviorInfo集合
     * @return UserBehaviorInfoVO集合
     */
    List<UserBehaviorInfoVo> convertVoList(List<UserBehaviorInfo> userBehaviorInfoList);

    /**
     * description: 用户创建行为 如果存在则删除，不存在则创建
     * author: YY
     * method: userInsertUserBehaviorInfo
     * date: 2025/4/14 16:54
     * param:
     * param: userBehaviorInfo
     * return: int
     **/
    int userInsertUserBehaviorInfo(UserBehaviorInfo userBehaviorInfo);
}
