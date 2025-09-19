package com.lz.picture.model.dto.statistics;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 图片统计
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-19  22:36
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PictureStatisticsRequest extends BasePictureStatisticsRequest {

    /**
     * 分类编号
     */
    private String categoryId;

    /**
     * 上传用户编号
     */
    private String userId;

    /**
     * 状态
     */
    private String pictureStatus;

    /**
     * 所属空间编号
     */
    private String spaceId;

    /**
     * 所属文件夹编号
     */
    private String folderId;

    /**
     * 删除（0否 1是）
     */
    private String isDelete;

}
