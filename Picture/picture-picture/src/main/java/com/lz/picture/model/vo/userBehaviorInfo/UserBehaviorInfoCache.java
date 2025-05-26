package com.lz.picture.model.vo.userBehaviorInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.picture.model.domain.UserBehaviorInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户行为Vo对象 p_user_behavior_info
 *
 * @author YY
 * @date 2025-04-12
 */
@Data
public class UserBehaviorInfoCache implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 行为类型
     */
    @Excel(name = "行为类型")
    private String behaviorType;

    /**
     * 用户编号
     */
    @Excel(name = "用户编号")
    private String userId;

    /**
     * 目标类型
     */
    @Excel(name = "目标类型")
    private String targetType;

    /**
     * 对象转封装类
     *
     * @param userBehaviorInfo UserBehaviorInfo实体对象
     * @return UserBehaviorInfoVo
     */
    public static UserBehaviorInfoCache objToVo(UserBehaviorInfo userBehaviorInfo) {
        if (userBehaviorInfo == null) {
            return null;
        }
        UserBehaviorInfoCache userBehaviorInfoVo = new UserBehaviorInfoCache();
        BeanUtils.copyProperties(userBehaviorInfo, userBehaviorInfoVo);
        return userBehaviorInfoVo;
    }

    /**
     * 对象列表封装
     */
    public static List<UserBehaviorInfoCache> objToVo(List<UserBehaviorInfo> userBehaviorInfoList) {
        return userBehaviorInfoList.stream().map(UserBehaviorInfoCache::objToVo).collect(Collectors.toList());
    }
}
