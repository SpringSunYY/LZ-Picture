package com.lz.picture.model.vo.userReportInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.UserReportInfo;
/**
 * 用户举报信息Vo对象 p_user_report_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class UserReportInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 举报编号 */
    @Excel(name = "举报编号")
    private String reportId;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private String userId;

    /** 目标类型（0图片 1用户 2空间） */
    @Excel(name = "目标类型", readConverterExp = "0=图片,1=用户,2=空间")
    private String targetType;

    /** 目标对象编号 */
    @Excel(name = "目标对象编号")
    private Long targetId;

    /** 封面快照（图片URL/用户头像URL/空间封面URL） */
    @Excel(name = "封面快照", readConverterExp = "图=片URL/用户头像URL/空间封面URL")
    private String targetCover;

    /** 举报原因 */
    @Excel(name = "举报原因")
    private String reason;

    /** 举报时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "举报时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 审核状态（0待审核 1通过 2拒绝） */
    @Excel(name = "审核状态", readConverterExp = "0=待审核,1=通过,2=拒绝")
    private Long reviewStatus;

    /** 审核信息 */
    @Excel(name = "审核信息")
    private String reviewMessage;

    /** 审核人编号 */
    @Excel(name = "审核人编号")
    private Long reviewUserId;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;


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
