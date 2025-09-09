package com.lz.user.model.dto.uStatisticsInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.user.model.domain.UStatisticsInfo;
/**
 * 统计信息Query对象 u_u_statistics_info
 *
 * @author YY
 * @date 2025-09-09
 */
@Data
public class UStatisticsInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 统计编号 */
    private String statisticsId;

    /** 统计类型 */
    private String type;

    /** 统计名称 */
    private String statisticsName;

    /** 公共KEY */
    private String commonKey;

    /** KEY */
    private String statisticsKey;

    /** 期数 */
    private Long stages;

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
     * @param uStatisticsInfoQuery 查询对象
     * @return UStatisticsInfo
     */
    public static UStatisticsInfo queryToObj(UStatisticsInfoQuery uStatisticsInfoQuery) {
        if (uStatisticsInfoQuery == null) {
            return null;
        }
        UStatisticsInfo uStatisticsInfo = new UStatisticsInfo();
        BeanUtils.copyProperties(uStatisticsInfoQuery, uStatisticsInfo);
        return uStatisticsInfo;
    }
}
