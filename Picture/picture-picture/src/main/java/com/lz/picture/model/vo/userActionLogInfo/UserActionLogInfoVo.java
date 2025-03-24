package com.lz.picture.model.vo.userActionLogInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.UserActionLogInfo;
/**
 * 用户行为日志Vo对象 p_user_action_log_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class UserActionLogInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 日志编号 */
    @Excel(name = "日志编号")
    private String actionId;

    /** 搜索记录编号 */
    @Excel(name = "搜索记录编号")
    private String searchId;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private String userId;

    /** 行为类型（0点击 1收藏 2下载 3关注用户 4查看空间） */
    @Excel(name = "行为类型", readConverterExp = "0=点击,1=收藏,2=下载,3=关注用户,4=查看空间")
    private String actionType;

    /** 目标类型（0图片 1空间 2用户） */
    @Excel(name = "目标类型", readConverterExp = "0=图片,1=空间,2=用户")
    private String targetType;

    /** 目标对象编号 */
    @Excel(name = "目标对象编号")
    private String targetId;

    /** 行为时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "行为时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 设备唯一标识 */
    @Excel(name = "设备唯一标识")
    private String deviceId;

    /** 浏览器类型 */
    @Excel(name = "浏览器类型")
    private String browser;

    /** 操作系统 */
    @Excel(name = "操作系统")
    private String os;

    /** 平台 */
    @Excel(name = "平台")
    private String platform;

    /** IP属地（如贵州省） */
    @Excel(name = "IP属地", readConverterExp = "如=贵州省")
    private String ipAddress;


     /**
     * 对象转封装类
     *
     * @param userActionLogInfo UserActionLogInfo实体对象
     * @return UserActionLogInfoVo
     */
    public static UserActionLogInfoVo objToVo(UserActionLogInfo userActionLogInfo) {
        if (userActionLogInfo == null) {
            return null;
        }
        UserActionLogInfoVo userActionLogInfoVo = new UserActionLogInfoVo();
        BeanUtils.copyProperties(userActionLogInfo, userActionLogInfoVo);
        return userActionLogInfoVo;
    }
}
