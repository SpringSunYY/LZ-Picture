package com.lz.user.model.dto.userRelationInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.user.model.domain.UserRelationInfo;
/**
 * 用户关系Vo对象 u_user_relation_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class UserRelationInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 关系ID */
    private String relationId;

    /** 用户 */
    private String userId;

    /** 关联用户 */
    private String relationUserId;

    /** 关系类型（0=关注 1=互关 2=拉黑） */
    private String relationType;

    /**
     * 对象转封装类
     *
     * @param userRelationInfoEdit 编辑对象
     * @return UserRelationInfo
     */
    public static UserRelationInfo editToObj(UserRelationInfoEdit userRelationInfoEdit) {
        if (userRelationInfoEdit == null) {
            return null;
        }
        UserRelationInfo userRelationInfo = new UserRelationInfo();
        BeanUtils.copyProperties(userRelationInfoEdit, userRelationInfo);
        return userRelationInfo;
    }
}
