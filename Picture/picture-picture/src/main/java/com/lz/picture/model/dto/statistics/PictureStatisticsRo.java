package com.lz.picture.model.dto.statistics;

import lombok.Data;

import java.io.Serializable;

/**
 * 图片统计RO
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-19  22:22
 * @Version: 1.0
 */
@Data
public class PictureStatisticsRo implements Serializable {
    private String createTime;
    private String uploadType;
    private Long total;
}
