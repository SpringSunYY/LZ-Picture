package com.lz.picture.model.dto.pictureInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 热门图片查询参数
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-07-14  22:54
 * @Version: 1.0
 */
@Data
public class PictureInfoHotRequest implements Serializable {
    private String type;
    private Integer pageNum;
    private Integer pageSize;
}
