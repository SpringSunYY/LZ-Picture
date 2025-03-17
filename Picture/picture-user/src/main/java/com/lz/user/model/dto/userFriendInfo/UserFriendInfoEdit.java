package com.lz.user.model.dto.userFriendInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.user.model.domain.UserFriendInfo;
/**
 * 用户好友关系Vo对象 u_user_friend_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class UserFriendInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 关系 */
    private String relationId;

    /** 用户 */
    private String userId;

    /** 好友用户 */
    private String friendUserId;

    /**
     * 对象转封装类
     *
     * @param userFriendInfoEdit 编辑对象
     * @return UserFriendInfo
     */
    public static UserFriendInfo editToObj(UserFriendInfoEdit userFriendInfoEdit) {
        if (userFriendInfoEdit == null) {
            return null;
        }
        UserFriendInfo userFriendInfo = new UserFriendInfo();
        BeanUtils.copyProperties(userFriendInfoEdit, userFriendInfo);
        return userFriendInfo;
    }
}
