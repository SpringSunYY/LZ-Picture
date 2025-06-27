package com.lz.picture.model.dto.spaceDilatationInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.SpaceDilatationInfo;
/**
 * 空间扩容信息Vo对象 p_space_dilatation_info
 *
 * @author YY
 * @date 2025-06-28
 */
@Data
public class SpaceDilatationInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 申请编号 */
    private String dilatationId;

    /** 扩容KEY */
    private String dilatationKey;

    /** 空间编号 */
    private String spaceId;

    /** 空间名称 */
    private String spaceName;

    /** 缩略图 URL */
    private String thumbnailUrl;

    /** 扩容类型 */
    private String dilatationType;

    /** 扩容单价 */
    private Long dilatationUnit;

    /** 扩容总数 */
    private Long dilatationTotal;

    /** 消耗积分 */
    private Long pointsTotal;

    /** 用户 */
    private String userId;

    /**
     * 对象转封装类
     *
     * @param spaceDilatationInfoEdit 编辑对象
     * @return SpaceDilatationInfo
     */
    public static SpaceDilatationInfo editToObj(SpaceDilatationInfoEdit spaceDilatationInfoEdit) {
        if (spaceDilatationInfoEdit == null) {
            return null;
        }
        SpaceDilatationInfo spaceDilatationInfo = new SpaceDilatationInfo();
        BeanUtils.copyProperties(spaceDilatationInfoEdit, spaceDilatationInfo);
        return spaceDilatationInfo;
    }
}
