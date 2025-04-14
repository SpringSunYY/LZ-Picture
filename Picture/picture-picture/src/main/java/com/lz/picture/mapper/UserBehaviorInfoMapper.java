package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.UserBehaviorInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lz.picture.model.vo.userBehaviorInfo.UserBehaviorInfoStaticVo;

/**
 * 用户行为Mapper接口
 *
 * @author YY
 * @date 2025-04-12
 */
public interface UserBehaviorInfoMapper extends BaseMapper<UserBehaviorInfo>
{
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
     * 删除用户行为
     *
     * @param behaviorId 用户行为主键
     * @return 结果
     */
    public int deleteUserBehaviorInfoByBehaviorId(String behaviorId);

    /**
     * 批量删除用户行为
     *
     * @param behaviorIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserBehaviorInfoByBehaviorIds(String[] behaviorIds);

    /**
     * description: 查询用户行为每种类型的行为统计信息
     * author: YY
     * method: staticBehaviorInfo
     * date: 2025/4/15 00:25
     * param:
     * param: behaviorInfo
     * return: java.util.List<com.lz.picture.model.vo.userBehaviorInfo.UserBehaviorInfoStaticVo>
     **/
    List<UserBehaviorInfoStaticVo> staticBehaviorInfo(UserBehaviorInfo behaviorInfo);
}
