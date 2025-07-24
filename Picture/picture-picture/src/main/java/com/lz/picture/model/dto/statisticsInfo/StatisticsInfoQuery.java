package com.lz.picture.model.dto.statisticsInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.picture.model.domain.StatisticsInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 统计信息Query对象 p_statistics_info
 *
 * @author YY
 * @date 2025-07-20
 */
@Data
public class StatisticsInfoQuery implements Serializable {
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
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param statisticsInfoQuery 查询对象
     * @return StatisticsInfo
     */
    public static StatisticsInfo queryToObj(StatisticsInfoQuery statisticsInfoQuery) {
        if (statisticsInfoQuery == null) {
            return null;
        }
        StatisticsInfo statisticsInfo = new StatisticsInfo();
        BeanUtils.copyProperties(statisticsInfoQuery, statisticsInfo);
        return statisticsInfo;
    }
}
