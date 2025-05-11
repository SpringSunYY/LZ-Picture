package com.lz.config.model.dto.fileLogInfo;

import lombok.Data;

/**
 * 更新图片日志
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-11  14:39
 * @Version: 1.0
 */
@Data
public class FileLogUpdate {

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 图片URL
     */
    private String pictureUrl;

    /**
     * 缩略图URL
     */
    private String thumbnailUrl;

    /**
     * 状态
     */
    private String logStatus;

    /**
     * 日志类型（0图片 1空间封面 2头像）
     */
    private String logType;

    /**
     * 日志编号
     */
    private String logId;
}
