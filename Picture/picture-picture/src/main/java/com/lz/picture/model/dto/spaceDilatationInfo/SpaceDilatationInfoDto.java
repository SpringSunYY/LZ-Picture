package com.lz.picture.model.dto.spaceDilatationInfo;

import com.lz.picture.model.domain.SpaceDilatationInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 空间扩容信息Vo对象 p_space_dilatation_info
 * 扩容返回内容
 *
 * @author YY
 * @date 2025-06-28
 */
@Data
public class SpaceDilatationInfoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 扩容类型
     */
    private String dilatationType;

    /**
     * 扩容单价
     */
    private Long dilatationUnit;

    /**
     * 扩容总数
     */
    private Long dilatationTotal;

    /**
     * 消耗积分
     */
    private Long pointsTotal;


    /**
     * 对象转封装类
     *
     * @param spaceDilatationInfoInsert 插入对象
     * @return SpaceDilatationInfoInsert
     */
    public static SpaceDilatationInfo insertToObj(SpaceDilatationInfoDto spaceDilatationInfoInsert) {
        if (spaceDilatationInfoInsert == null) {
            return null;
        }
        SpaceDilatationInfo spaceDilatationInfo = new SpaceDilatationInfo();
        BeanUtils.copyProperties(spaceDilatationInfoInsert, spaceDilatationInfo);
        return spaceDilatationInfo;
    }
}
