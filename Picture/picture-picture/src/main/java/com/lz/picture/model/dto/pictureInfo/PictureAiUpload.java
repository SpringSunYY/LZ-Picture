package com.lz.picture.model.dto.pictureInfo;

import jakarta.validation.constraints.NotBlank;
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
public class PictureAiUpload {

    /**
     * 图片名称
     */
    @NotBlank(message = "图片名称不能为空")
    private String name;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 分类编号
     */
    @NotBlank(message = "分类不能为空，且必须是AI下分类")
    private String categoryId;


    /**
     * 所属空间编号
     */
    @NotBlank(message = "所属空间编号不能为空")
    private String spaceId;

    /**
     * 所属文件夹编号
     */
    private String folderId;

    /**
     * 图片标签
     */
    private List<String> tags;

    @NotBlank(message = "图片状态不能为空")
    private String pictureStatus;

    private Long pointsNeed;

    private String userId;

    @NotBlank(message = "请选择生成记录")
    private String logId;

}
