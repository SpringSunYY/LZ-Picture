package com.lz.picture.model.dto.userReportInfo;

import java.util.Map;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.UserReportInfo;
/**
 * 用户举报信息Query对象 p_user_report_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class UserReportInfoQuery implements Serializable
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

    /** 举报原因 */
    private String reason;

    /** 举报时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 审核状态（0待审核 1通过 2拒绝） */
    private Long reviewStatus;

    /** 审核人编号 */
    private Long reviewUserId;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reviewTime;

    /** 请求参数 */
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
