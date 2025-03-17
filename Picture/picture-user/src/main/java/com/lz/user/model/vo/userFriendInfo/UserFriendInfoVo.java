package com.lz.user.model.vo.userFriendInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.user.model.domain.UserFriendInfo;
/**
 * 用户好友关系Vo对象 u_user_friend_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class UserFriendInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 关系 */
    @Excel(name = "关系")
    private String relationId;

    /** 用户 */
    @Excel(name = "用户")
    private String userId;

    /** 好友用户 */
    @Excel(name = "好友用户")
    private String friendUserId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


     /**
     * 对象转封装类
     *
     * @param userFriendInfo UserFriendInfo实体对象
     * @return UserFriendInfoVo
     */
    public static UserFriendInfoVo objToVo(UserFriendInfo userFriendInfo) {
        if (userFriendInfo == null) {
            return null;
        }
        UserFriendInfoVo userFriendInfoVo = new UserFriendInfoVo();
        BeanUtils.copyProperties(userFriendInfo, userFriendInfoVo);
        return userFriendInfoVo;
    }
}
