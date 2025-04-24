package com.lz.common.manager.file.model;

import lombok.Data;

/**
 * PictureFileInfo
 * 图片信息
 *
 * @project: Picture
 * @package: com.lz.common.manager.file.model
 * @author: YY
 * @createTime: 2025-04-24  20:02
 * @version: 1.0
 */
@Data
public class PictureFileInfo {
    /**
     * 没有后缀文件名
     */
    private String fileNameNotSuffix;

    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 新的文件名称
     */
    private String newFileName;

    /**
     * 压缩文件后缀
     */
    private String compressedSuffix;

    /**
     * 压缩文件名
     */
    private String compressedFileName;

    /**
     * 压缩文件路径
     */
    private String compressedFilePath;
}
