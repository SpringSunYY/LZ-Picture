package com.lz.picture.model.dto.userViewLogInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.UserViewLogInfo;
/**
 * 用户浏览记录Vo对象 p_user_view_log_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class UserViewLogInfoInsert implements Serializable
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

    /** 封面URL */
    private String targetCover;

    /**
     * 对象转封装类
     *
     * @param userViewLogInfoInsert 插入对象
     * @return UserViewLogInfoInsert
     */
    public static UserViewLogInfo insertToObj(UserViewLogInfoInsert userViewLogInfoInsert) {
        if (userViewLogInfoInsert == null) {
            return null;
        }
        UserViewLogInfo userViewLogInfo = new UserViewLogInfo();
        BeanUtils.copyProperties(userViewLogInfoInsert, userViewLogInfo);
        return userViewLogInfo;
    }
}
