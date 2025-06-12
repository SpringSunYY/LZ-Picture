package com.lz.picture.model.dto.pictureApiSearch;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户图片搜索请求参数
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-12  23:20
 * @Version: 1.0
 */
@Data
public class PictureApiSearchRequest {
    /**
     * api名称
     */
    @NotEmpty(message = "api不能为空")
    private String api;
    /**
     * 模型名称
     */
    @NotEmpty(message = "模型不能为空")
    private String model;
    /**
     * 关键词
     */
    @NotEmpty(message = "关键词不能为空")
    @Length(max = 50, message = "关键词不能超过50个字符")
    private String keyword;
    /**
     * 用户id
     */
    private String userId;
}
