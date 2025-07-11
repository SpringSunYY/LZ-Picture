package com.lz.config.model.vo.fileLogInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.config.model.domain.FileLogInfo;

/**
 * 文件日志Vo对象 c_file_log_info
 *
 * @author YY
 * @date 2025-05-11
 */
@Data
public class FileLogInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 日志编号
     */
    @Excel(name = "日志编号")
    private String logId;

    /**
     * 用户编号
     */
    @Excel(name = "用户编号")
    private String userId;

    /**
     * 目标对象
     */
    @Excel(name = "目标对象")
    private String targetId;

    /**
     * 目标内容
     */
    @Excel(name = "目标内容")
    private String targetContent;

    /**
     * 域名URL
     */
    @Excel(name = "域名URL")
    private String dnsUrl;

    /**
     * 文件路径
     */
    @Excel(name = "文件路径")
    private String fileUrl;

    /**
     * 文件类型
     */
    @Excel(name = "文件类型")
    private String fileType;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String logStatus;

    /**
     * 存储类型（0官方 1阿里云）
     */
    @Excel(name = "存储类型", readConverterExp = "0=官方,1=阿里云")
    private String ossType;

    /**
     * 日志类型（0图片 1空间封面 2头像）
     */
    @Excel(name = "日志类型", readConverterExp = "0=图片,1=空间封面,2=头像")
    private String logType;

    /**
     * 是否压缩（0是 1否）
     */
    @Excel(name = "是否压缩", readConverterExp = "0=是,1=否")
    private String isCompress;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 删除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "删除时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date deleteTime;

    /**
     * IP地址
     */
    @Excel(name = "IP地址")
    private String ipAddr;

    /**
     * 设备唯一标识
     */
    @Excel(name = "设备唯一标识")
    private String deviceId;

    /**
     * 浏览器类型
     */
    @Excel(name = "浏览器类型")
    private String browser;

    /**
     * 操作系统
     */
    @Excel(name = "操作系统")
    private String os;

    /**
     * 平台
     */
    @Excel(name = "平台")
    private String platform;

    /**
     * IP属地
     */
    @Excel(name = "IP属地")
    private String ipAddress;


    /**
     * 对象转封装类
     *
     * @param fileLogInfo FileLogInfo实体对象
     * @return FileLogInfoVo
     */
    public static FileLogInfoVo objToVo(FileLogInfo fileLogInfo) {
        if (fileLogInfo == null) {
            return null;
        }
        FileLogInfoVo fileLogInfoVo = new FileLogInfoVo();
        BeanUtils.copyProperties(fileLogInfo, fileLogInfoVo);
        return fileLogInfoVo;
    }
}
