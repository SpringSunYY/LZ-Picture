package com.lz.picture.model.dto.statistics;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 关键词搜索
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-18  15:54
 * @Version: 1.0
 */
@Data
public class PictureStatisticsRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "开始时间不能为空")
    private String startDate;
    @NotBlank(message = "结束时间不能为空")
    private String endDate;
}
