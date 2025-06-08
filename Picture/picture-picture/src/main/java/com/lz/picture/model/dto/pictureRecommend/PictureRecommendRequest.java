package com.lz.picture.model.dto.pictureRecommend;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户推荐
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-06  20:43
 * @Version: 1.0
 */
@Data
public class PictureRecommendRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private String userId;
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
     * 审核状态（0待审核 1通过 2拒绝）
     */
    private String reviewStatus;

    /**
     * 是否删除
     */
    private String isDelete;
}
