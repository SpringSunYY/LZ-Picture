package com.lz.common.manager.file.model;

import lombok.Data;

/**
 * 批量下载图片响应参数
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-07-25  16:17
 * @Version: 1.0
 */
@Data
public class BatchDownloadFileDto {
    private String ossFilePath;
    private String localPath;
}
