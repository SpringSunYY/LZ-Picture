package com.lz.user.model.vo.statistics;

import com.lz.common.core.domain.statistics.vo.MapStatisticsVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户登录地点统计
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-17  16:56
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginLocationStatisticsVo {
    private String date;
    private List<MapStatisticsVo> datas;
}
