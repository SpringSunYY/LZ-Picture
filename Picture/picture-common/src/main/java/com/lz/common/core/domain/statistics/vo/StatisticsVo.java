package com.lz.common.core.domain.statistics.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 统计返回vo
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-18  16:01
 * @Version: 1.0
 */
@Data
public class StatisticsVo implements Serializable {
    private String name;
    private Long value;
}
