package com.lz.common.manager.file.model;

import com.lz.common.annotation.Excel;
import lombok.Data;

/**
 * Project: Picture
 * Package: com.lz.common.manager.file.model
 * Author: YY
 * CreateTime: 2025-03-29  20:29
 * Description: PictureResponse
 * Version: 1.0
 */
@Data
public class PictureResponse {
    /**
     * 图片URL
     */
    private String pictureUrl;

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
    private Long picScale;

    /**
     * 图片格式
     */
    private String picFormat;

    /**
     * 缩略图URL
     */
    private String thumbnailUrl;

}
