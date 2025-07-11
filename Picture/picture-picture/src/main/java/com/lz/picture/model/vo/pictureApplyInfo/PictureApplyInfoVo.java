package com.lz.picture.model.vo.pictureApplyInfo;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureApplyInfo;
/**
 * 图片申请信息Vo对象 p_picture_apply_info
 *
 * @author YY
 * @date 2025-06-17
 */
@Data
public class PictureApplyInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 申请编号 */
    @Excel(name = "申请编号")
    private String applyId;

    /** 图片编号 */
    @Excel(name = "图片编号")
    private String pictureId;

    /** 图片名称 */
    @Excel(name = "图片名称")
    private String pictureName;

    /** 缩略图 URL */
    @Excel(name = "缩略图 URL")
    private String thumbnailUrl;

    /** 申请类型 */
    @Excel(name = "申请类型")
    private String applyType;

    /** 申请理由 */
    @Excel(name = "申请理由")
    private String applyReason;

    /** 证明图片 */
    @Excel(name = "证明图片")
    private String applyImage;

    /** 证明文件 */
    @Excel(name = "证明文件")
    private String applyFile;

    /** 联系方式 */
    @Excel(name = "联系方式")
    private String contact;

    /** 所需积分 */
    @Excel(name = "所需积分")
    private Long pointsNeed;

    /** 所需金额 */
    @Excel(name = "所需金额")
    private BigDecimal priceNeed;

    /** 用户 */
    @Excel(name = "用户")
    private String userId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 审核状态 */
    @Excel(name = "审核状态")
    private String reviewStatus;

    /** 审核信息 */
    @Excel(name = "审核信息")
    private String reviewMessage;

    /** 审核人编号 */
    @Excel(name = "审核人编号")
    private Long reviewUserId;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;


     /**
     * 对象转封装类
     *
     * @param pictureApplyInfo PictureApplyInfo实体对象
     * @return PictureApplyInfoVo
     */
    public static PictureApplyInfoVo objToVo(PictureApplyInfo pictureApplyInfo) {
        if (pictureApplyInfo == null) {
            return null;
        }
        PictureApplyInfoVo pictureApplyInfoVo = new PictureApplyInfoVo();
        BeanUtils.copyProperties(pictureApplyInfo, pictureApplyInfoVo);
        return pictureApplyInfoVo;
    }
}
