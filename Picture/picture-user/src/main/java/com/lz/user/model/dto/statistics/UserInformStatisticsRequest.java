package com.lz.user.model.dto.statistics;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户信息请求
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-13  16:30
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInformStatisticsRequest extends UserStatisticsRequest{
    @Min(value = 1, message = "页码不能小于1")
    private Long pageNum;
    @Min(value = 10, message = "页大小不能小于10")
    @Max(value = 100, message = "页大小不能大于100")
    private Long pageSize;
}
