package com.lz.picture.model.dto.userBehaviorInfo;

import java.util.Map;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.UserBehaviorInfo;
/**
 * 用户行为Query对象 p_user_behavior_info
 *
 * @author YY
 * @date 2025-04-12
 */
@Data
public class UserBehaviorInfoQuery implements Serializable
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
    private BigDecimal score;

    /** 图片分类 */
    private String categoryId;

    /** 空间 */
    private String spaceId;

    /** 图片标签 */
    private String tags;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

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

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param userBehaviorInfoQuery 查询对象
     * @return UserBehaviorInfo
     */
    public static UserBehaviorInfo queryToObj(UserBehaviorInfoQuery userBehaviorInfoQuery) {
        if (userBehaviorInfoQuery == null) {
            return null;
        }
        UserBehaviorInfo userBehaviorInfo = new UserBehaviorInfo();
        BeanUtils.copyProperties(userBehaviorInfoQuery, userBehaviorInfo);
        return userBehaviorInfo;
    }
}
