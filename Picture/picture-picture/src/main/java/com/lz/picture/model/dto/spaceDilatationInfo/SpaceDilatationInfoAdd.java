package com.lz.picture.model.dto.spaceDilatationInfo;

import com.lz.picture.model.domain.SpaceDilatationInfo;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 空间扩容信息Vo对象 p_space_dilatation_info
 * 用户添加空间扩容
 * @author YY
 * @date 2025-06-28
 */
@Data
public class SpaceDilatationInfoAdd implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    /** 空间编号 */
    @NotEmpty(message = "空间编号不能为空")
    private String spaceId;

    /** 扩容类型 */
    @NotEmpty(message = "扩容类型不能为空")
    private String dilatationType;

    /** 扩容总数 */
    private Long dilatationTotal;


    /**
     * 对象转封装类
     *
     * @param spaceDilatationInfoInsert 插入对象
     * @return SpaceDilatationInfoInsert
     */
    public static SpaceDilatationInfo insertToObj(SpaceDilatationInfoAdd spaceDilatationInfoInsert) {
        if (spaceDilatationInfoInsert == null) {
            return null;
        }
        SpaceDilatationInfo spaceDilatationInfo = new SpaceDilatationInfo();
        BeanUtils.copyProperties(spaceDilatationInfoInsert, spaceDilatationInfo);
        return spaceDilatationInfo;
    }
}
