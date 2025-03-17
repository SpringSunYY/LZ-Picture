package com.lz.user.model.dto.userBindingInfo;

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
import com.lz.user.model.domain.UserBindingInfo;
/**
 * 用户第三方账号绑定Query对象 u_user_binding_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class UserBindingInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 绑定ID */
    private String bindingId;

    /** 用户ID */
    private String userId;

    /** 绑定类型 */
    private String bindingType;

    /** 绑定时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date bindingTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param userBindingInfoQuery 查询对象
     * @return UserBindingInfo
     */
    public static UserBindingInfo queryToObj(UserBindingInfoQuery userBindingInfoQuery) {
        if (userBindingInfoQuery == null) {
            return null;
        }
        UserBindingInfo userBindingInfo = new UserBindingInfo();
        BeanUtils.copyProperties(userBindingInfoQuery, userBindingInfo);
        return userBindingInfo;
    }
}
