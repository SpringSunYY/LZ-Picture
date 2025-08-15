package com.lz.config.model.dto.fileLogInfo;

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
import com.lz.config.model.domain.FileLogInfo;

/**
 * 文件日志Query对象 c_file_log_info
 *
 * @author YY
 * @date 2025-05-11
 */
@Data
public class FileLogInfoQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 日志编号
     */
    private String logId;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 目标对象
     */
    private String targetId;

    /**
     * 目标内容
     */
    private String targetContent;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 状态
     */
    private String logStatus;

    /**
     * 日志类型（0图片 1空间封面 2头像）
     */
    private String logType;

    /**
     * 是否压缩（0是 1否）
     */
    private String isCompress;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 删除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deleteTime;

    /**
     * IP地址
     */
    private String ipAddr;

    /**
     * 设备唯一标识
     */
    private String deviceId;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 平台
     */
    private String platform;

    /**
     * IP属地
     */
    private String ipAddress;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param fileLogInfoQuery 查询对象
     * @return FileLogInfo
     */
    public static FileLogInfo queryToObj(FileLogInfoQuery fileLogInfoQuery) {
        if (fileLogInfoQuery == null) {
            return null;
        }
        FileLogInfo fileLogInfo = new FileLogInfo();
        BeanUtils.copyProperties(fileLogInfoQuery, fileLogInfo);
        return fileLogInfo;
    }
}
