package com.lz.config.model.dto.fileLogInfo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.config.model.domain.FileLogInfo;

/**
 * 文件日志Vo对象 c_file_log_info
 *
 * @author YY
 * @date 2025-05-11
 */
@Data
public class FileLogInfoInsert implements Serializable {
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
     * 域名URL
     */
    private String dnsUrl;

    /**
     * 文件路径
     */
    private String fileUrl;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 状态
     */
    private String logStatus;

    /**
     * 存储类型（0官方 1阿里云）
     */
    private String ossType;

    /**
     * 日志类型（0图片 1空间封面 2头像）
     */
    private String logType;

    /**
     * 是否压缩（0是 1否）
     */
    private String isCompress;

    /**
     * 对象转封装类
     *
     * @param fileLogInfoInsert 插入对象
     * @return FileLogInfoInsert
     */
    public static FileLogInfo insertToObj(FileLogInfoInsert fileLogInfoInsert) {
        if (fileLogInfoInsert == null) {
            return null;
        }
        FileLogInfo fileLogInfo = new FileLogInfo();
        BeanUtils.copyProperties(fileLogInfoInsert, fileLogInfo);
        return fileLogInfo;
    }
}
