package com.lz.picture.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.lz.common.constant.HttpStatus;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.utils.StringUtils;

import java.math.BigDecimal;

import com.lz.common.utils.DateUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.common.utils.ip.IpUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.picture.model.enums.PUserBehaviorTargetType;
import com.lz.picture.model.enums.PUserBehaviorType;
import com.lz.picture.model.enums.PUserBehaviorTypeScore;
import com.lz.picture.model.vo.userBehaviorInfo.UserBehaviorInfoStaticVo;
import com.lz.picture.strategy.userBehaviorInfoStrategy.UserBehaviorInfoStrategyExecutor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.picture.mapper.UserBehaviorInfoMapper;
import com.lz.picture.model.domain.UserBehaviorInfo;
import com.lz.picture.service.IUserBehaviorInfoService;
import com.lz.picture.model.dto.userBehaviorInfo.UserBehaviorInfoQuery;
import com.lz.picture.model.vo.userBehaviorInfo.UserBehaviorInfoVo;

import static com.lz.picture.model.enums.PUserBehaviorTypeScore.USER_BEHAVIOR_TYPE_SCORE_0;

/**
 * 用户行为Service业务层处理
 *
 * @author YY
 * @date 2025-04-12
 */
@Service
public class UserBehaviorInfoServiceImpl extends ServiceImpl<UserBehaviorInfoMapper, UserBehaviorInfo> implements IUserBehaviorInfoService {
    @Resource
    private UserBehaviorInfoMapper userBehaviorInfoMapper;

    @Resource
    @Lazy
    private UserBehaviorInfoStrategyExecutor userBehaviorInfoStrategyExecutor;

    //region mybatis代码

    /**
     * 查询用户行为
     *
     * @param behaviorId 用户行为主键
     * @return 用户行为
     */
    @Override
    public UserBehaviorInfo selectUserBehaviorInfoByBehaviorId(String behaviorId) {
        return userBehaviorInfoMapper.selectUserBehaviorInfoByBehaviorId(behaviorId);
    }

    /**
     * 查询用户行为列表
     *
     * @param userBehaviorInfo 用户行为
     * @return 用户行为
     */
    @Override
    public List<UserBehaviorInfo> selectUserBehaviorInfoList(UserBehaviorInfo userBehaviorInfo) {
        return userBehaviorInfoMapper.selectUserBehaviorInfoList(userBehaviorInfo);
    }

    /**
     * 新增用户行为
     *
     * @param userBehaviorInfo 用户行为
     * @return 结果
     */
    @Override
    public int insertUserBehaviorInfo(UserBehaviorInfo userBehaviorInfo) {
        userBehaviorInfo.setCreateTime(DateUtils.getNowDate());
        return userBehaviorInfoMapper.insertUserBehaviorInfo(userBehaviorInfo);
    }

    /**
     * 修改用户行为
     *
     * @param userBehaviorInfo 用户行为
     * @return 结果
     */
    @Override
    public int updateUserBehaviorInfo(UserBehaviorInfo userBehaviorInfo) {
        return userBehaviorInfoMapper.updateUserBehaviorInfo(userBehaviorInfo);
    }

    /**
     * 批量删除用户行为
     *
     * @param behaviorIds 需要删除的用户行为主键
     * @return 结果
     */
    @Override
    public int deleteUserBehaviorInfoByBehaviorIds(String[] behaviorIds) {
        return userBehaviorInfoMapper.deleteUserBehaviorInfoByBehaviorIds(behaviorIds);
    }

    /**
     * 删除用户行为信息
     *
     * @param behaviorId 用户行为主键
     * @return 结果
     */
    @Override
    public int deleteUserBehaviorInfoByBehaviorId(String behaviorId) {
        return userBehaviorInfoMapper.deleteUserBehaviorInfoByBehaviorId(behaviorId);
    }

    //endregion
    @Override
    public QueryWrapper<UserBehaviorInfo> getQueryWrapper(UserBehaviorInfoQuery userBehaviorInfoQuery) {
        QueryWrapper<UserBehaviorInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = userBehaviorInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String behaviorId = userBehaviorInfoQuery.getBehaviorId();
        queryWrapper.eq(StringUtils.isNotEmpty(behaviorId), "behavior_id", behaviorId);

        String behaviorType = userBehaviorInfoQuery.getBehaviorType();
        queryWrapper.eq(StringUtils.isNotEmpty(behaviorType), "behavior_type", behaviorType);

        String userId = userBehaviorInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        String targetType = userBehaviorInfoQuery.getTargetType();
        queryWrapper.eq(StringUtils.isNotEmpty(targetType), "target_type", targetType);

        String targetId = userBehaviorInfoQuery.getTargetId();
        queryWrapper.eq(StringUtils.isNotEmpty(targetId), "target_id", targetId);

        String targetContent = userBehaviorInfoQuery.getTargetContent();
        queryWrapper.eq(StringUtils.isNotEmpty(targetContent), "target_content", targetContent);

        Double score = userBehaviorInfoQuery.getScore();
        queryWrapper.eq(StringUtils.isNotNull(score), "score", score);

        String categoryId = userBehaviorInfoQuery.getCategoryId();
        queryWrapper.eq(StringUtils.isNotEmpty(categoryId), "category_id", categoryId);

        String spaceId = userBehaviorInfoQuery.getSpaceId();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceId), "space_id", spaceId);

        String tags = userBehaviorInfoQuery.getTags();
        queryWrapper.like(StringUtils.isNotEmpty(tags), "tags", tags);

        Date createTime = userBehaviorInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        String deviceId = userBehaviorInfoQuery.getDeviceId();
        queryWrapper.eq(StringUtils.isNotEmpty(deviceId), "device_id", deviceId);

        String browser = userBehaviorInfoQuery.getBrowser();
        queryWrapper.eq(StringUtils.isNotEmpty(browser), "browser", browser);

        String os = userBehaviorInfoQuery.getOs();
        queryWrapper.eq(StringUtils.isNotEmpty(os), "os", os);

        String platform = userBehaviorInfoQuery.getPlatform();
        queryWrapper.eq(StringUtils.isNotEmpty(platform), "platform", platform);

        String ipAddress = userBehaviorInfoQuery.getIpAddress();
        queryWrapper.eq(StringUtils.isNotEmpty(ipAddress), "ip_address", ipAddress);

        return queryWrapper;
    }

    @Override
    public List<UserBehaviorInfoVo> convertVoList(List<UserBehaviorInfo> userBehaviorInfoList) {
        if (StringUtils.isEmpty(userBehaviorInfoList)) {
            return Collections.emptyList();
        }
        return userBehaviorInfoList.stream().map(UserBehaviorInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public UserBehaviorInfo userInsertUserBehaviorInfo(UserBehaviorInfo userBehaviorInfo) {
        //校验数据 获取分数
        checkType(userBehaviorInfo);
        DeviceInfo deviceInfo = IpUtils.getDeviceInfo();
        BeanUtils.copyProperties(deviceInfo, userBehaviorInfo);
        //根据不同类型来赋值 策略模式
        return userBehaviorInfoStrategyExecutor.executeGetUserBehaviorInfo(userBehaviorInfo);
    }

    @Override
    public List<UserBehaviorInfoStaticVo> staticBehaviorInfo(UserBehaviorInfo behaviorInfo) {
        return userBehaviorInfoMapper.staticBehaviorInfo(behaviorInfo);
    }

    private static void checkType(UserBehaviorInfo userBehaviorInfo) {
        //判断目标类型
        Optional<PUserBehaviorTargetType> userBehaviorTargetType = PUserBehaviorTargetType.getEnumByValue(userBehaviorInfo.getTargetType());
        ThrowUtils.throwIf(StringUtils.isNull(userBehaviorTargetType), "目标类型错误");
        //判断行为类型
        Optional<PUserBehaviorType> behaviorType = PUserBehaviorType.getEnumByValue(userBehaviorInfo.getBehaviorType());
        ThrowUtils.throwIf(behaviorType.isEmpty(), "行为类型错误");
        //根据行为类型获取分数
        PUserBehaviorType behaviorTypeValue = behaviorType.get();
        Optional<PUserBehaviorTypeScore> scoreOptional = PUserBehaviorTypeScore.getEnumByValue(behaviorTypeValue.getValue());
        ThrowUtils.throwIf(scoreOptional.isEmpty(), "行为类型分数未配置");
        userBehaviorInfo.setScore(scoreOptional.get().getScore());
    }

}
