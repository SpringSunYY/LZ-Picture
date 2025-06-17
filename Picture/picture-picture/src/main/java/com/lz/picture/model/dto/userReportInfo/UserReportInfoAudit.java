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
public class UserReportInfoAudit implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 举报编号
     */
    private String reportId;

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
     * 对象转封装类
     *
     * @param userReportInfoAudit 编辑对象
     * @return UserReportInfo
     */
    public static UserReportInfo editToObj(UserReportInfoAudit userReportInfoAudit) {
        if (userReportInfoAudit == null) {
            return null;
        }
        UserReportInfo userReportInfo = new UserReportInfo();
        BeanUtils.copyProperties(userReportInfoAudit, userReportInfo);
        return userReportInfo;
    }
}
