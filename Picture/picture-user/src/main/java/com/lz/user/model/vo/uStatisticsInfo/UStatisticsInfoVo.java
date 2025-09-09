package com.lz.user.model.vo.uStatisticsInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.user.model.domain.UStatisticsInfo;
/**
 * 统计信息Vo对象 u_u_statistics_info
 *
 * @author YY
 * @date 2025-09-09
 */
@Data
public class UStatisticsInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 统计编号 */
    @Excel(name = "统计编号")
    private String statisticsId;

    /** 统计类型 */
    @Excel(name = "统计类型")
    private String type;

    /** 统计名称 */
    @Excel(name = "统计名称")
    private String statisticsName;

    /** 公共KEY */
    @Excel(name = "公共KEY")
    private String commonKey;

    /** KEY */
    @Excel(name = "KEY")
    private String statisticsKey;

    /** 期数 */
    @Excel(name = "期数")
    private Long stages;

    /** 统计内容 */
    @Excel(name = "统计内容")
    private String content;

    /** 统计内容 */
    @Excel(name = "统计内容")
    private String extendContent;

    /** 描述 */
    @Excel(name = "描述")
    private String remark;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


     /**
     * 对象转封装类
     *
     * @param uStatisticsInfo UStatisticsInfo实体对象
     * @return UStatisticsInfoVo
     */
    public static UStatisticsInfoVo objToVo(UStatisticsInfo uStatisticsInfo) {
        if (uStatisticsInfo == null) {
            return null;
        }
        UStatisticsInfoVo uStatisticsInfoVo = new UStatisticsInfoVo();
        BeanUtils.copyProperties(uStatisticsInfo, uStatisticsInfoVo);
        return uStatisticsInfoVo;
    }
}
