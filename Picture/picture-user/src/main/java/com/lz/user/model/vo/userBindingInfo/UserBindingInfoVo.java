package com.lz.user.model.vo.userBindingInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.user.model.domain.UserBindingInfo;
/**
 * 用户第三方账号绑定Vo对象 u_user_binding_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class UserBindingInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 绑定ID */
    @Excel(name = "绑定ID")
    private String bindingId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private String userId;

    /** 绑定类型 */
    @Excel(name = "绑定类型")
    private String bindingType;

    /** 第三方唯一标识 */
    @Excel(name = "第三方唯一标识")
    private String identifier;

    /** 扩展配置 */
    @Excel(name = "扩展配置")
    private String extendConfig;

    /** 绑定时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "绑定时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date bindingTime;


     /**
     * 对象转封装类
     *
     * @param userBindingInfo UserBindingInfo实体对象
     * @return UserBindingInfoVo
     */
    public static UserBindingInfoVo objToVo(UserBindingInfo userBindingInfo) {
        if (userBindingInfo == null) {
            return null;
        }
        UserBindingInfoVo userBindingInfoVo = new UserBindingInfoVo();
        BeanUtils.copyProperties(userBindingInfo, userBindingInfoVo);
        return userBindingInfoVo;
    }
}
