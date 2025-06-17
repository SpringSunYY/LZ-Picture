package com.lz.picture.model.dto.pictureApplyInfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.PictureApplyInfo;
/**
 * 图片申请信息Vo对象 p_picture_apply_info
 *
 * @author YY
 * @date 2025-06-17
 */
@Data
public class PictureApplyInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 申请编号 */
    private String applyId;

    /** 图片编号 */
    private String pictureId;

    /** 图片名称 */
    private String pictureName;

    /** 缩略图 URL */
    private String thumbnailUrl;

    /** 申请类型 */
    private String applyType;

    /** 申请理由 */
    private String applyReason;

    /** 证明图片 */
    private String applyImage;

    /** 证明文件 */
    private String applyFile;

    /** 联系方式 */
    private String contact;

    /** 所需积分 */
    private Long pointsNeed;

    /** 所需金额 */
    private BigDecimal priceNeed;

    /** 审核状态 */
    private String reviewStatus;

    /** 审核信息 */
    private String reviewMessage;

    /**
     * 对象转封装类
     *
     * @param pictureApplyInfoInsert 插入对象
     * @return PictureApplyInfoInsert
     */
    public static PictureApplyInfo insertToObj(PictureApplyInfoInsert pictureApplyInfoInsert) {
        if (pictureApplyInfoInsert == null) {
            return null;
        }
        PictureApplyInfo pictureApplyInfo = new PictureApplyInfo();
        BeanUtils.copyProperties(pictureApplyInfoInsert, pictureApplyInfo);
        return pictureApplyInfo;
    }
}
