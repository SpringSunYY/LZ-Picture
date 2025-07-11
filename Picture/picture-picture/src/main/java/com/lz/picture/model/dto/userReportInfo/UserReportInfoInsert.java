package com.lz.picture.model.dto.userReportInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class UserReportInfoInsert implements Serializable {
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
     * 举报原因
     */
    private String reason;

    /**
     * 审核状态（0待审核 1通过 2拒绝）
     */
    private String reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reviewTime;

    /**
     * 对象转封装类
     *
     * @param userReportInfoInsert 插入对象
     * @return UserReportInfoInsert
     */
    public static UserReportInfo insertToObj(UserReportInfoInsert userReportInfoInsert) {
        if (userReportInfoInsert == null) {
            return null;
        }
        UserReportInfo userReportInfo = new UserReportInfo();
        BeanUtils.copyProperties(userReportInfoInsert, userReportInfo);
        return userReportInfo;
    }
}
