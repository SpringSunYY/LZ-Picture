package com.lz.picture.model.dto.statisticsInfo;

import com.lz.picture.model.vo.pictureInfo.PictureInfoStatisticsVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 图片统计dto
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-07-20  16:27
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PictureStatisticsDto {
    /**
     * 已经统计的map
     */
    private LinkedHashMap<String, PictureInfoStatisticsVo> statisticsMap;
    /**
     * 所有的map
     */
    private LinkedHashMap<String, PictureInfoStatisticsVo> allMap;
    /**
     * 已统计的list，存入缓存的content
     */
    private List<PictureInfoStatisticsVo> statisticsList;
    /**
     * 所有的list，extend_content
     */
    private List<PictureInfoStatisticsVo> allList;
}
