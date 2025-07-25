package com.lz.picture.model.dto.statisticsInfo;

import com.lz.picture.model.domain.StatisticsInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统计信息Query对象 p_statistics_info
 *
 * @author YY
 * @date 2025-07-20
 */
@Data
public class StatisticsInfoRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 统计类型
     */
    private String type;


    /**
     * 公共KEY
     */
    private String commonKey;

    /**
     * KEY
     */
    private String statisticsKey;

    /**
     * 期数
     */
    private Long stages;

    /**
     * 对象转封装类
     *
     * @param statisticsInfoQuery 查询对象
     * @return StatisticsInfo
     */
    public static StatisticsInfo requestToObj(StatisticsInfoRequest statisticsInfoQuery) {
        if (statisticsInfoQuery == null) {
            return null;
        }
        StatisticsInfo statisticsInfo = new StatisticsInfo();
        BeanUtils.copyProperties(statisticsInfoQuery, statisticsInfo);
        return statisticsInfo;
    }
}
