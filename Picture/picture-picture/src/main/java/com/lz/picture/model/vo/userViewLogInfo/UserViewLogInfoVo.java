package com.lz.picture.model.vo.userViewLogInfo;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.UserViewLogInfo;
/**
 * 用户浏览记录Vo对象 p_user_view_log_info
 *
 * @author YY
 * @date 2025-04-12
 */
@Data
public class UserViewLogInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 记录编号 */
    @Excel(name = "记录编号")
    private String viewId;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private String userId;

    /** 目标类型 */
    @Excel(name = "目标类型")
    private String targetType;

    /** 目标对象 */
    @Excel(name = "目标对象")
    private String targetId;

    /** 目标内容 */
    @Excel(name = "目标内容")
    private String targetContent;

    /** 分数 */
    @Excel(name = "分数")
    private BigDecimal score;

    /** 图片分类 */
    @Excel(name = "图片分类")
    private String categoryId;

    /** 空间 */
    @Excel(name = "空间")
    private String spaceId;

    /** 图片标签 */
    @Excel(name = "图片标签")
    private String tags;

    /** 封面 */
    @Excel(name = "封面")
    private String targetCover;

    /** 查看时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "查看时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
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

    /** IP属地 */
    @Excel(name = "IP属地")
    private String ipAddress;


     /**
     * 对象转封装类
     *
     * @param userViewLogInfo UserViewLogInfo实体对象
     * @return UserViewLogInfoVo
     */
    public static UserViewLogInfoVo objToVo(UserViewLogInfo userViewLogInfo) {
        if (userViewLogInfo == null) {
            return null;
        }
        UserViewLogInfoVo userViewLogInfoVo = new UserViewLogInfoVo();
        BeanUtils.copyProperties(userViewLogInfo, userViewLogInfoVo);
        return userViewLogInfoVo;
    }
}
