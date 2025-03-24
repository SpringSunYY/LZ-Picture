package com.lz.picture.model.dto.userViewLogInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.UserViewLogInfo;
/**
 * 用户浏览记录Query对象 p_user_view_log_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class UserViewLogInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 浏览记录编号 */
    private String viewId;

    /** 用户编号 */
    private String userId;

    /** 目标类型（0图片 1用户 2空间） */
    private String targetType;

    /** 目标对象编号 */
    private String targetId;

    /** 图片分类 */
    private String categoryId;

    /** 图片标签 */
    private String tags;

    /** 查看时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param userViewLogInfoQuery 查询对象
     * @return UserViewLogInfo
     */
    public static UserViewLogInfo queryToObj(UserViewLogInfoQuery userViewLogInfoQuery) {
        if (userViewLogInfoQuery == null) {
            return null;
        }
        UserViewLogInfo userViewLogInfo = new UserViewLogInfo();
        BeanUtils.copyProperties(userViewLogInfoQuery, userViewLogInfo);
        return userViewLogInfo;
    }
}
