package com.lz.picture.model.dto.userReportInfo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.UserReportInfo;
/**
 * 用户举报信息Vo对象 p_user_report_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class UserReportInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 举报编号 */
    private String reportId;

    /** 用户编号 */
    private String userId;

    /** 目标类型（0图片 1用户 2空间） */
    private String targetType;

    /** 目标对象编号 */
    private Long targetId;

    /** 封面快照（图片URL/用户头像URL/空间封面URL） */
    private String targetCover;

    /** 举报原因 */
    private String reason;

    /** 审核状态（0待审核 1通过 2拒绝） */
    private Long reviewStatus;

    /** 审核信息 */
    private String reviewMessage;

    /** 审核人编号 */
    private Long reviewUserId;

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
