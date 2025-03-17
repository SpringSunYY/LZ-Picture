package com.lz.user.model.dto.userFriendInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.user.model.domain.UserFriendInfo;
/**
 * 用户好友关系Query对象 u_user_friend_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class UserFriendInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 关系 */
    private String relationId;

    /** 用户 */
    private String userId;

    /** 好友用户 */
    private String friendUserId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param userFriendInfoQuery 查询对象
     * @return UserFriendInfo
     */
    public static UserFriendInfo queryToObj(UserFriendInfoQuery userFriendInfoQuery) {
        if (userFriendInfoQuery == null) {
            return null;
        }
        UserFriendInfo userFriendInfo = new UserFriendInfo();
        BeanUtils.copyProperties(userFriendInfoQuery, userFriendInfo);
        return userFriendInfo;
    }
}
