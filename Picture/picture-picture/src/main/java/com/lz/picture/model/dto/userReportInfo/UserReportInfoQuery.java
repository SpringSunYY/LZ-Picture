package com.lz.picture.model.dto.userReportInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.picture.model.domain.UserReportInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 用户举报信息Query对象 p_user_report_info
 *
 * @author YY
 * @date 2025-06-17
 */
@Data
public class UserReportInfoQuery implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 举报编号
     */
    private String reportId;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 举报类型
     */
    private String reportType;

    /**
     * 目标类型（0图片 1用户 2空间）
     */
    private String targetType;

    /**
     * 目标对象编号
     */
    private Long targetId;

    /**
     * 封面快照（图片URL/用户头像URL/空间封面URL）
     */
    private String targetCover;

    /**
     * 举报原因
     */
    private String reason;

    /**
     * 举报时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 审核状态（0待审核 1通过 2拒绝）
     */
    private Long reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    /**
     * 审核人编号
     */
    private Long reviewUserId;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reviewTime;

    /**
     * 设备唯一标识
     */
    private String deviceId;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 平台
     */
    private String platform;

    /**
     * IP地址
     */
    private String ipAddr;

    /**
     * IP属地
     */
    private String ipAddress;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param userReportInfoQuery 查询对象
     * @return UserReportInfo
     */
    public static UserReportInfo queryToObj(UserReportInfoQuery userReportInfoQuery) {
        if (userReportInfoQuery == null) {
            return null;
        }
        UserReportInfo userReportInfo = new UserReportInfo();
        BeanUtils.copyProperties(userReportInfoQuery, userReportInfo);
        return userReportInfo;
    }
}
