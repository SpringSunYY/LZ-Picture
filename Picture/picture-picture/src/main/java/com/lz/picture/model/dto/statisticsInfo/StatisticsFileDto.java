package com.lz.picture.model.dto.statisticsInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统计文件
 * 生而有翼
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-07-25  15:35
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsFileDto {
    private String fileName;
    private String filePath;
}
