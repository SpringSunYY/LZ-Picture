package com.lz.user.model.dto.uStatisticsInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.user.model.domain.UStatisticsInfo;
/**
 * 统计信息Vo对象 u_u_statistics_info
 *
 * @author YY
 * @date 2025-09-09
 */
@Data
public class UStatisticsInfoInsert implements Serializable
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

    /** 统计内容 */
    private String content;

    /** 统计内容 */
    private String extendContent;

    /** 描述 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param uStatisticsInfoInsert 插入对象
     * @return UStatisticsInfoInsert
     */
    public static UStatisticsInfo insertToObj(UStatisticsInfoInsert uStatisticsInfoInsert) {
        if (uStatisticsInfoInsert == null) {
            return null;
        }
        UStatisticsInfo uStatisticsInfo = new UStatisticsInfo();
        BeanUtils.copyProperties(uStatisticsInfoInsert, uStatisticsInfo);
        return uStatisticsInfo;
    }
}
