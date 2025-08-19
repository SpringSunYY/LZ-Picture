package com.lz.picture.model.dto.pictureInfo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 图片推荐信息
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-05  22:41
 * @Version: 1.0
 */
@Data
public class PictureInfoDetailRecommendRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 图片编号
     */
    @NotEmpty(message = "图片编号不能为空")
    private String pictureId;

    /**
     * 当前记录起始索引
     */
    @NotNull(message = "当前记录起始索引不能为空")
    @Min(value = 0, message = "当前记录起始索引不能小于0")
    private Integer currentPage;

    /**
     * 每页显示记录数
     */
    @NotNull(message = "每页显示记录数不能为空")
    @Min(value = 1, message = "每页显示记录数不能小于1")
    @Max(value = 50, message = "每页显示记录数不能大于50")
    private Integer pageSize;

    private Integer offset;

    /**
     * 图片状态（0公共 1私有）
     */
    private String pictureStatus;

    /**
     * 上传类型
     */
    private String uploadType;
}
