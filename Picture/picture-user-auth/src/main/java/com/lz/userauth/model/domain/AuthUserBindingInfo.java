package com.lz.userauth.model.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.lz.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 用户第三方账号绑定对象 u_user_binding_info
 *
 * @author YY
 * @date 2025-03-17
 */
@TableName("u_user_binding_info")
@Data
public class AuthUserBindingInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 绑定ID
     */
    @Excel(name = "绑定ID")
    @TableId(value = "binding_id", type = IdType.ASSIGN_ID)
    private String bindingId;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private String userId;

    /**
     * 绑定类型
     */
    @Excel(name = "绑定类型")
    private String bindingType;

    /**
     * 第三方唯一标识
     */
    @Excel(name = "第三方唯一标识")
    private String identifier;

    /**
     * 扩展配置
     */
    @Excel(name = "扩展配置")
    private String extendConfig;

    /**
     * 绑定时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "绑定时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date bindingTime;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
