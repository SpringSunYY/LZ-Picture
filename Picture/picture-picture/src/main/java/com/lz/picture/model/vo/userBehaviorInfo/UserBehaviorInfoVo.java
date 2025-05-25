package com.lz.picture.model.vo.userBehaviorInfo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.UserBehaviorInfo;

/**
 * 用户行为Vo对象 p_user_behavior_info
 *
 * @author YY
 * @date 2025-04-12
 */
@Data
public class UserBehaviorInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 行为编号
     */
    @Excel(name = "行为编号")
    private String behaviorId;

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
     * 目标对象
     */
    @Excel(name = "目标对象")
    private String targetId;

    /**
     * 目标内容
     */
    @Excel(name = "目标内容")
    private String targetContent;

    /**
     * 分数
     */
    @Excel(name = "分数")
    private Double score;

    /**
     * 分享链接
     */
    @Excel(name = "分享链接")
    private String shareLink;

    /**
     * 图片分类
     */
    @Excel(name = "图片分类")
    private String categoryId;

    /**
     * 空间
     */
    @Excel(name = "空间")
    private String spaceId;

    /**
     * 图片标签
     */
    @Excel(name = "图片标签")
    private String tags;

    /**
     * 封面
     */
    @Excel(name = "封面")
    private String targetCover;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 是否统计（0否 1是）
     */
    @Excel(name = "是否统计", readConverterExp = "0=否,1=是")
    private String hasStatistics;

    /**
     * 设备唯一标识
     */
    @Excel(name = "设备唯一标识")
    private String deviceId;

    /**
     * 浏览器类型
     */
    @Excel(name = "浏览器类型")
    private String browser;

    /**
     * 操作系统
     */
    @Excel(name = "操作系统")
    private String os;

    /**
     * 平台
     */
    @Excel(name = "平台")
    private String platform;

    /**
     * IP地址
     */
    @Excel(name = "IP地址")
    private String ipAddr;

    /**
     * IP属地
     */
    @Excel(name = "IP属地")
    private String ipAddress;


    /**
     * 对象转封装类
     *
     * @param userBehaviorInfo UserBehaviorInfo实体对象
     * @return UserBehaviorInfoVo
     */
    public static UserBehaviorInfoVo objToVo(UserBehaviorInfo userBehaviorInfo) {
        if (userBehaviorInfo == null) {
            return null;
        }
        UserBehaviorInfoVo userBehaviorInfoVo = new UserBehaviorInfoVo();
        BeanUtils.copyProperties(userBehaviorInfo, userBehaviorInfoVo);
        return userBehaviorInfoVo;
    }
}
