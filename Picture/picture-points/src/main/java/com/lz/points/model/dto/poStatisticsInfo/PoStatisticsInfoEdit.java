package com.lz.points.model.dto.poStatisticsInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.points.model.domain.PoStatisticsInfo;
/**
 * 统计信息Vo对象 po_po_statistics_info
 *
 * @author YY
 * @date 2025-09-23
 */
@Data
public class PoStatisticsInfoEdit implements Serializable
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
     * @param poStatisticsInfoEdit 编辑对象
     * @return PoStatisticsInfo
     */
    public static PoStatisticsInfo editToObj(PoStatisticsInfoEdit poStatisticsInfoEdit) {
        if (poStatisticsInfoEdit == null) {
            return null;
        }
        PoStatisticsInfo poStatisticsInfo = new PoStatisticsInfo();
        BeanUtils.copyProperties(poStatisticsInfoEdit, poStatisticsInfo);
        return poStatisticsInfo;
    }
}
