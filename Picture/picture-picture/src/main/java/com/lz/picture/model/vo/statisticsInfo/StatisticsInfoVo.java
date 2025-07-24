package com.lz.picture.model.vo.statisticsInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.picture.model.domain.StatisticsInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 统计信息Vo对象 p_statistics_info
 *
 * @author YY
 * @date 2025-07-20
 */
@Data
public class StatisticsInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 统计编号
     */
    @Excel(name = "统计编号")
    private String statisticsId;

    /**
     * 统计类型
     */
    @Excel(name = "统计类型")
    private String type;

    /**
     * 统计名称
     */
    @Excel(name = "统计名称")
    private String statisticsName;

    /**
     * 公共KEY
     */
    @Excel(name = "公共KEY")
    private String commonKey;

    /**
     * KEY
     */
    @Excel(name = "KEY")
    private String statisticsKey;

    /**
     * 期数
     */
    @Excel(name = "期数")
    private Long stages;

    /**
     * 统计内容
     */
    @Excel(name = "统计内容")
    private String content;

    /**
     * 统计内容
     */
    @Excel(name = "统计内容")
    private String extendContent;

    /**
     * 描述
     */
    @Excel(name = "描述")
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /**
     * 对象转封装类
     *
     * @param statisticsInfo StatisticsInfo实体对象
     * @return StatisticsInfoVo
     */
    public static StatisticsInfoVo objToVo(StatisticsInfo statisticsInfo) {
        if (statisticsInfo == null) {
            return null;
        }
        StatisticsInfoVo statisticsInfoVo = new StatisticsInfoVo();
        BeanUtils.copyProperties(statisticsInfo, statisticsInfoVo);
        return statisticsInfoVo;
    }
}
