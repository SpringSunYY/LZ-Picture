package com.lz.picture.model.vo.pictureApiSearch;

import lombok.Data;

import java.util.List;

/**
 * 图片api搜索返回VO
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-12  23:24
 * @Version: 1.0
 */
@Data
public class PictureApiSearchVo {
    /**
     * api名称
     */
    private String api;
    /**
     * 模型名称
     */
    private String model;
    /**
     * 数量
     */
    private Integer count;

    /**
     * 最大数
     */
    private Integer maxCount;
    /**
     * 关键词
     */
    private String keyword;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 图片地址
     */
    private List<String> urls;
}
