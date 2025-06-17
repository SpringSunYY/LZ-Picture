package com.lz.picture.model.dto.userReportInfo;

import com.lz.picture.model.domain.UserReportInfo;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户举报信息Vo对象 p_user_report_info
 * 用户举报
 *
 * @author YY
 * @date 2025-06-17
 */
@Data
public class UserReportInfoAdd implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 目标编号
     */
    private String targetId;

    /**
     * 举报类型
     */
    @NotEmpty(message = "举报类型不能为空")
    private String reportType;

    /**
     * 目标类型（0图片 1用户 2空间）
     */
    @NotEmpty(message = "目标类型不能为空")
    private String targetType;

    /**
     * 举报原因
     */
    @NotEmpty(message = "举报原因不能为空")
    private String reason;

    /**
     * 对象转封装类
     *
     * @param userReportInfoInsert 插入对象
     * @return UserReportInfoInsert
     */
    public static UserReportInfo insertToObj(UserReportInfoAdd userReportInfoInsert) {
        if (userReportInfoInsert == null) {
            return null;
        }
        UserReportInfo userReportInfo = new UserReportInfo();
        BeanUtils.copyProperties(userReportInfoInsert, userReportInfo);
        return userReportInfo;
    }
}
