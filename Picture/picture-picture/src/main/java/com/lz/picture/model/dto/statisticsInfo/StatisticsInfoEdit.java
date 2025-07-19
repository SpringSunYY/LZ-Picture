package com.lz.picture.model.dto.statisticsInfo;

import com.lz.picture.model.domain.StatisticsInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 统计信息Vo对象 p_statistics_info
 *
 * @author YY
 * @date 2025-07-20
 */
@Data
public class StatisticsInfoEdit implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 统计编号
     */
    private String statisticsId;

    /**
     * 统计类型
     */
    private String type;

    /**
     * 统计名称
     */
    private String statisticsName;

    /**
     * KEY
     */
    private String statisticsKey;

    /**
     * 期数
     */
    private Long stages;

    /**
     * 统计内容
     */
    private String content;

    /**
     * 统计内容
     */
    private String extendContent;

    /**
     * 描述
     */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param statisticsInfoEdit 编辑对象
     * @return StatisticsInfo
     */
    public static StatisticsInfo editToObj(StatisticsInfoEdit statisticsInfoEdit) {
        if (statisticsInfoEdit == null) {
            return null;
        }
        StatisticsInfo statisticsInfo = new StatisticsInfo();
        BeanUtils.copyProperties(statisticsInfoEdit, statisticsInfo);
        return statisticsInfo;
    }
}
