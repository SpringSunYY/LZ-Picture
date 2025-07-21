package com.lz.picture.model.vo.pictureInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 图片下载信息返回信息
 * 永远灿烂
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-07-21  23:26
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PictureDownloadVo implements Serializable {
    /**
     * 图片编号
     */
    private String pictureId;
    /**
     * 图片URL
     */
    private String pictureUrl;
    /**
     * 所需积分
     */
    private Long pointsNeed;

    /**
     * 所需金额
     */
    private BigDecimal priceNeed;
}
