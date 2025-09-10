package com.lz.user.model.dto.statistics;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户统计信息请求参数
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-10  15:48
 * @Version: 1.0
 */
@Data
public class UserStatisticsRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "开始时间不能为空")
    private String startDate;
    @NotBlank(message = "结束时间不能为空")
    private String endDate;

    /**
     * 删除标记（0=未删除 1=已删除）
     */
    private String isDelete;
}
