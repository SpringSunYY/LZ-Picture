package com.lz.ai.model.dto.userUsageLogInfo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.ai.model.domain.UserUsageLogInfo;
/**
 * 用户AI使用记录Vo对象 ai_user_usage_log_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class UserUsageLogInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 记录编号 */
    private String logId;

    /** 用户编号 */
    private String userId;

    /** 模型编号 */
    private String modelId;

    /** 输入参数 */
    private String inputParams;

    /** 返回结果 */
    private String outputResult;

    /** 请求时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestTime;

    /** 请求时长（毫秒） */
    private Long requestDuration;

    /** 消耗Tokens数量 */
    private Long tokensUsed;

    /** 消耗积分 */
    private Long pointsUsed;

    /** 使用类型（0AI扩图 1AI编辑 2AI搜索） */
    private String usageType;

    /** 目标编号 */
    private String targetId;

    /** 状态（0成功 1失败 2超时） */
    private String logStatus;

    /** 模型返回码 */
    private String aiStatusCode;

    /** 失败原因 */
    private String failReason;

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

    /** 删除（0正常 1删除） */
    private String isDelete;

    /**
     * 对象转封装类
     *
     * @param userUsageLogInfoEdit 编辑对象
     * @return UserUsageLogInfo
     */
    public static UserUsageLogInfo editToObj(UserUsageLogInfoEdit userUsageLogInfoEdit) {
        if (userUsageLogInfoEdit == null) {
            return null;
        }
        UserUsageLogInfo userUsageLogInfo = new UserUsageLogInfo();
        BeanUtils.copyProperties(userUsageLogInfoEdit, userUsageLogInfo);
        return userUsageLogInfo;
    }
}
