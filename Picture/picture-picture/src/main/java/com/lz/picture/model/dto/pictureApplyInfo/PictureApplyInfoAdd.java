package com.lz.picture.model.dto.pictureApplyInfo;

import com.lz.picture.model.domain.PictureApplyInfo;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
public class PictureApplyInfoAdd implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图片编号
     */
    @NotEmpty(message = "图片编号不能为空")
    private String pictureId;


    /**
     * 申请类型
     */
    @NotEmpty(message = "申请类型不能为空")
    private String applyType;

    /**
     * 申请理由
     */
    @NotEmpty(message = "申请理由不能为空")
    private String applyReason;

    /**
     * 证明图片
     */
    private String applyImage;

    /**
     * 证明文件
     */
    private String applyFile;

    /**
     * 联系方式
     */
    @NotEmpty(message = "联系方式不能为空")
    @Size(max = 512, message = "联系方式不能超过512个字符")
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
     * 对象转封装类
     *
     * @param pictureApplyInfoInsert 插入对象
     * @return PictureApplyInfoInsert
     */
    public static PictureApplyInfo insertToObj(PictureApplyInfoAdd pictureApplyInfoInsert) {
        if (pictureApplyInfoInsert == null) {
            return null;
        }
        PictureApplyInfo pictureApplyInfo = new PictureApplyInfo();
        BeanUtils.copyProperties(pictureApplyInfoInsert, pictureApplyInfo);
        return pictureApplyInfo;
    }
}
