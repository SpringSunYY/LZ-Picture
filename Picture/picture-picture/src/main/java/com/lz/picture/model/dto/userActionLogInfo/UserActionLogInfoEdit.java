package com.lz.picture.model.dto.userActionLogInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.UserActionLogInfo;
/**
 * 用户行为日志Vo对象 p_user_action_log_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class UserActionLogInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 日志编号 */
    private String actionId;

    /** 搜索记录编号 */
    private String searchId;

    /** 用户编号 */
    private String userId;

    /** 行为类型（0点击 1收藏 2下载 3关注用户 4查看空间） */
    private String actionType;

    /** 目标类型（0图片 1空间 2用户） */
    private String targetType;

    /** 目标对象编号 */
    private String targetId;

    /** 设备唯一标识 */
    private String deviceId;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 平台 */
    private String platform;

    /**
     * 对象转封装类
     *
     * @param userActionLogInfoEdit 编辑对象
     * @return UserActionLogInfo
     */
    public static UserActionLogInfo editToObj(UserActionLogInfoEdit userActionLogInfoEdit) {
        if (userActionLogInfoEdit == null) {
            return null;
        }
        UserActionLogInfo userActionLogInfo = new UserActionLogInfo();
        BeanUtils.copyProperties(userActionLogInfoEdit, userActionLogInfo);
        return userActionLogInfo;
    }
}
