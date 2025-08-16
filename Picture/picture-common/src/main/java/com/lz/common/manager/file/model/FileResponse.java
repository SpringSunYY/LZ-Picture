package com.lz.common.manager.file.model;

import lombok.Data;

/**
 * 图片文件返回
 * Project: Picture
 * Package: com.lz.common.manager.file.model
 * Author: YY
 * CreateTime: 2025-03-29  20:29
 * Description: PictureFileResponse
 * Version: 1.0
 */
@Data
public class FileResponse {
    /**
     * 图片URL
     */
    private String url;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 图片体积（字节）
     */
    private Long picSize;

    /**
     * 图片宽度
     */
    private Long picWidth;

    /**
     * 图片高度
     */
    private Long picHeight;

    /**
     * 宽高比例
     */
    private Double picScale;

    /**
     * 图片格式
     */
    private String picFormat;

    /**
     * 缩略图URL
     */
    private String thumbnailUrl;
}
