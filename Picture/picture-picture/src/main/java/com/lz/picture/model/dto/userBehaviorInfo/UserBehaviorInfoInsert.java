package com.lz.picture.model.dto.userBehaviorInfo;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.UserBehaviorInfo;
/**
 * 用户行为Vo对象 p_user_behavior_info
 *
 * @author YY
 * @date 2025-04-12
 */
@Data
public class UserBehaviorInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 行为编号 */
    private String behaviorId;

    /** 行为类型 */
    private String behaviorType;

    /** 用户编号 */
    private String userId;

    /** 目标类型 */
    private String targetType;

    /** 目标对象 */
    private String targetId;

    /** 目标内容 */
    private String targetContent;

    /** 分数 */
    private Double score;

    /** 分享链接 */
    private String shareLink;

    /** 图片分类 */
    private String categoryId;

    /** 空间 */
    private String spaceId;

    /** 图片标签 */
    private String tags;

    /** 封面 */
    private String targetCover;

    /** 设备唯一标识 */
    private String deviceId;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 平台 */
    private String platform;

    /** IP属地 */
    private String ipAddress;

    /**
     * 对象转封装类
     *
     * @param userBehaviorInfoInsert 插入对象
     * @return UserBehaviorInfoInsert
     */
    public static UserBehaviorInfo insertToObj(UserBehaviorInfoInsert userBehaviorInfoInsert) {
        if (userBehaviorInfoInsert == null) {
            return null;
        }
        UserBehaviorInfo userBehaviorInfo = new UserBehaviorInfo();
        BeanUtils.copyProperties(userBehaviorInfoInsert, userBehaviorInfo);
        return userBehaviorInfo;
    }
}
