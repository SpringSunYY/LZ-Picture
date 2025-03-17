package com.lz.user.model.dto.userBindingInfo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.user.model.domain.UserBindingInfo;
/**
 * 用户第三方账号绑定Vo对象 u_user_binding_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class UserBindingInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 绑定ID */
    private String bindingId;

    /** 用户ID */
    private String userId;

    /** 绑定类型 */
    private String bindingType;

    /** 第三方唯一标识 */
    private String identifier;

    /** 扩展配置 */
    private String extendConfig;

    /**
     * 对象转封装类
     *
     * @param userBindingInfoInsert 插入对象
     * @return UserBindingInfoInsert
     */
    public static UserBindingInfo insertToObj(UserBindingInfoInsert userBindingInfoInsert) {
        if (userBindingInfoInsert == null) {
            return null;
        }
        UserBindingInfo userBindingInfo = new UserBindingInfo();
        BeanUtils.copyProperties(userBindingInfoInsert, userBindingInfo);
        return userBindingInfo;
    }
}
