package com.lz.picture.model.vo.userReportInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.picture.model.domain.UserReportInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户举报信息Vo对象 p_user_report_info
 *
 * @author YY
 * @date 2025-06-17
 */
@Data
public class UserReportInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 举报编号
     */
    @Excel(name = "举报编号")
    private String reportId;

    /**
     * 用户编号
     */
    @Excel(name = "用户编号")
    private String userId;

    /**
     * 举报类型
     */
    @Excel(name = "举报类型")
    private String reportType;

    /**
     * 目标类型（0图片 1用户 2空间）
     */
    @Excel(name = "目标类型", readConverterExp = "0=图片,1=用户,2=空间")
    private String targetType;

    /**
     * 目标对象编号
     */
    @Excel(name = "目标对象编号")
    private String targetId;

    /**
     * 封面快照（图片URL/用户头像URL/空间封面URL）
     */
    @Excel(name = "封面快照")
    private String targetCover;

    /**
     * 目标内容
     */
    @Excel(name = "目标内容")
    private String targetContent;

    /**
     * 举报原因
     */
    @Excel(name = "举报原因")
    private String reason;

    /**
     * 联系方式
     */
    @Excel(name = "联系方式")
    private String contact;

    /**
     * 举报时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "举报时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 审核状态（0待审核 1通过 2拒绝）
     */
    @Excel(name = "审核状态", readConverterExp = "0=待审核,1=通过,2=拒绝")
    private String reviewStatus;

    /**
     * 审核信息
     */
    @Excel(name = "审核信息")
    private String reviewMessage;

    /**
     * 审核人编号
     */
    @Excel(name = "审核人编号")
    private Long reviewUserId;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

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
     * @param userReportInfo UserReportInfo实体对象
     * @return UserReportInfoVo
     */
    public static UserReportInfoVo objToVo(UserReportInfo userReportInfo) {
        if (userReportInfo == null) {
            return null;
        }
        UserReportInfoVo userReportInfoVo = new UserReportInfoVo();
        BeanUtils.copyProperties(userReportInfo, userReportInfoVo);
        return userReportInfoVo;
    }
}
