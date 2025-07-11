package com.lz.picture.model.dto.pictureApplyInfo;

import java.util.Map;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureApplyInfo;
/**
 * 图片申请信息Query对象 p_picture_apply_info
 *
 * @author YY
 * @date 2025-06-17
 */
@Data
public class PictureApplyInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 申请编号 */
    private String applyId;

    /** 图片编号 */
    private String pictureId;

    /** 图片名称 */
    private String pictureName;

    /** 申请类型 */
    private String applyType;

    /** 用户 */
    private String userId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 审核状态 */
    private String reviewStatus;

    /** 审核人编号 */
    private Long reviewUserId;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reviewTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param pictureApplyInfoQuery 查询对象
     * @return PictureApplyInfo
     */
    public static PictureApplyInfo queryToObj(PictureApplyInfoQuery pictureApplyInfoQuery) {
        if (pictureApplyInfoQuery == null) {
            return null;
        }
        PictureApplyInfo pictureApplyInfo = new PictureApplyInfo();
        BeanUtils.copyProperties(pictureApplyInfoQuery, pictureApplyInfo);
        return pictureApplyInfo;
    }
}
