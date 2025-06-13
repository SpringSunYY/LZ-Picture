package com.lz.picture.model.dto.pictureInfo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 图片URL上传请求参数
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-13  11:17
 * @Version: 1.0
 */
@Data
public class PictureUrlUpload {

    /**
     * 图片URL
     */
    @NotEmpty(message = "图片文件地址不能为空")
    private String url;

    /**
     * 图片名称
     */
    @NotEmpty(message = "图片名称不能为空")
    private String name;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 分类编号
     */
    private String categoryId;


    /**
     * 所属空间编号
     */
    @NotEmpty(message = "所属空间编号不能为空")
    private String spaceId;

    /**
     * 所属文件夹编号
     */
    private String folderId;

    /**
     * 图片标签
     */
    private List<String> tags;

}
