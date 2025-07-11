package com.lz.picture.model.dto.pictureInfo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 图片更多信息
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-13  11:54
 * @Version: 1.0
 */
@Data
public class PictureMoreInfo {
    /**
     * 原始URL
     */
    private String originUrl;

    /**
     * 所需积分
     */
    private Long pointsNeed;

    /**
     * 所需金额
     */
    private BigDecimal priceNeed;

    /**
     * 申请类型
     */
    private String applyType;
}
