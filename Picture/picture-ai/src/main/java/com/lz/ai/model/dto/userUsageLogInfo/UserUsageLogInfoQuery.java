package com.lz.ai.model.dto.userUsageLogInfo;

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
import com.lz.ai.model.domain.UserUsageLogInfo;
/**
 * 用户AI使用记录Query对象 ai_user_usage_log_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class UserUsageLogInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 记录编号 */
    private String logId;

    /** 用户编号 */
    private String userId;

    /** 模型编号 */
    private String modelId;

    /** 请求时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestTime;

    /** 使用类型（0AI扩图 1AI编辑 2AI搜索） */
    private String usageType;

    /** 目标编号 */
    private String targetId;

    /** 状态（0成功 1失败 2超时） */
    private String logStatus;

    /** 模型返回码 */
    private String aiStatusCode;

    /** 用户IP地址 */
    private String ipAddr;

    /** 设备唯一标识 */
    private String deviceId;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 平台 */
    private String platform;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 删除（0正常 1删除） */
    private String isDelete;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param userUsageLogInfoQuery 查询对象
     * @return UserUsageLogInfo
     */
    public static UserUsageLogInfo queryToObj(UserUsageLogInfoQuery userUsageLogInfoQuery) {
        if (userUsageLogInfoQuery == null) {
            return null;
        }
        UserUsageLogInfo userUsageLogInfo = new UserUsageLogInfo();
        BeanUtils.copyProperties(userUsageLogInfoQuery, userUsageLogInfo);
        return userUsageLogInfo;
    }
}
