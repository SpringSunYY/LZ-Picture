package com.lz.picture.model.dto.pictureApplyInfo;

import com.lz.picture.model.domain.PictureApplyInfo;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 图片申请信息Vo对象 p_picture_apply_info
 *
 * @author YY
 * @date 2025-06-17
 */
@Data
public class PictureApplyInfoEdit implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 申请编号
     */
    @NotEmpty(message = "申请编号不能为空")
    private String applyId;

    /**
     * 图片编号
     */
    @NotEmpty(message = "图片编号不能为空")
    private String pictureId;

    /**
     * 图片名称
     */
    @NotEmpty(message = "图片名称不能为空")
    private String pictureName;

    /**
     * 申请类型
     */
    @NotEmpty(message = "申请类型不能为空")
    private String applyType;

    /**
     * 申请理由
     */
    private String applyReason;

    /**
     * 联系方式
     */
    @NotEmpty(message = "联系方式不能为空")
    private String contact;

    /**
     * 所需积分
     */
    private Long pointsNeed;

    /**
     * 所需金额
     */
    private BigDecimal priceNeed;

    /**
     * 审核状态
     */
    private String reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    /**
     * 对象转封装类
     *
     * @param pictureApplyInfoEdit 编辑对象
     * @return PictureApplyInfo
     */
    public static PictureApplyInfo editToObj(PictureApplyInfoEdit pictureApplyInfoEdit) {
        if (pictureApplyInfoEdit == null) {
            return null;
        }
        PictureApplyInfo pictureApplyInfo = new PictureApplyInfo();
        BeanUtils.copyProperties(pictureApplyInfoEdit, pictureApplyInfo);
        return pictureApplyInfo;
    }
}
