package com.lz.picture.model.dto.userViewLogInfo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.UserViewLogInfo;

/**
 * 用户浏览记录Vo对象 p_user_view_log_info
 *
 * @author YY
 * @date 2025-04-12
 */
@Data
public class UserViewLogInfoEdit implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    private String viewId;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 目标类型
     */
    private String targetType;

    /**
     * 目标对象
     */
    private String targetId;

    /**
     * 目标内容
     */
    private String targetContent;

    /**
     * 分数
     */
    private Double score;

    /**
     * 图片分类
     */
    private String categoryId;

    /**
     * 空间
     */
    private String spaceId;

    /**
     * 图片标签
     */
    private String tags;

    /**
     * 封面
     */
    private String targetCover;


    /**
     * 对象转封装类
     *
     * @param userViewLogInfoEdit 编辑对象
     * @return UserViewLogInfo
     */
    public static UserViewLogInfo editToObj(UserViewLogInfoEdit userViewLogInfoEdit) {
        if (userViewLogInfoEdit == null) {
            return null;
        }
        UserViewLogInfo userViewLogInfo = new UserViewLogInfo();
        BeanUtils.copyProperties(userViewLogInfoEdit, userViewLogInfo);
        return userViewLogInfo;
    }
}
