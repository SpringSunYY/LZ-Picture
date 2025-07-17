package com.lz.picture.model.dto.statisticsInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.StatisticsInfo;
/**
 * 统计信息Query对象 p_statistics_info
 *
 * @author YY
 * @date 2025-07-17
 */
@Data
public class StatisticsInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 统计编号 */
    private String statisticsId;

    /** 统计粒度 */
    private String type;

    /** 统计名称 */
    private String statisticsName;

    /** KEY */
    private String statisticsKey;

    /** 期数 */
    private Long stages;

    /** 统计内容 */
    private String content;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 请求参数 */
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
