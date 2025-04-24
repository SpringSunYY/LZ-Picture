package com.lz.picture.model.vo.pictureInfo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureInfo;

/**
 * 图片信息Vo对象 p_picture_info
 *
 * @author YY
 * @date 2025-04-09
 */
@Data
public class PictureInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图片编号
     */
    @Excel(name = "图片编号")
    private String pictureId;

    /**
     * 图片URL
     */
    @Excel(name = "图片URL")
    private String pictureUrl;

    /**
     * 域名URL
     */
    @Excel(name = "域名URL")
    private String dnsUrl;

    /**
     * 图片名称
     */
    @Excel(name = "图片名称")
    private String name;

    /**
     * 简介
     */
    @Excel(name = "简介")
    private String introduction;

    /**
     * 分类编号
     */
    @Excel(name = "分类编号")
    private String categoryId;

    /**
     * 图片体积
     */
    @Excel(name = "图片体积")
    private Long picSize;

    /**
     * 图片宽度
     */
    @Excel(name = "图片宽度")
    private Long picWidth;

    /**
     * 图片高度
     */
    @Excel(name = "图片高度")
    private Long picHeight;

    /**
     * 宽高比例
     */
    @Excel(name = "宽高比例")
    private Double picScale;

    /**
     * 图片格式
     */
    @Excel(name = "图片格式")
    private String picFormat;

    /**
     * 所需积分
     */
    @Excel(name = "所需积分")
    private Long pointsNeed;

    /**
     * 上传用户编号
     */
    @Excel(name = "上传用户编号")
    private String userId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 编辑时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "编辑时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date editTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 图片状态（0公共 1私有）
     */
    @Excel(name = "图片状态", readConverterExp = "0=公共,1=私有")
    private String pictureStatus;

    /**
     * 审核状态（0待审核 1通过 2拒绝）
     */
    @Excel(name = "审核状态", readConverterExp = "0=待审核,1=通过,2=拒绝")
    private Long reviewStatus;

    /**
     * 审核信息
     */
    @Excel(name = "审核信息")
    private String reviewMessage;

    /**
     * 审核人编号
     */
    @Excel(name = "审核人编号")
    private Long reviewUserId;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    /**
     * 缩略图URL
     */
    @Excel(name = "缩略图URL")
    private String thumbnailUrl;

    /**
     * 所属空间编号
     */
    @Excel(name = "所属空间编号")
    private String spaceId;

    /**
     * 所属文件夹编号
     */
    @Excel(name = "所属文件夹编号")
    private String folderId;

    /**
     * 更多信息
     */
    @Excel(name = "更多信息")
    private String moreInfo;

    /**
     * 删除（0否 1是）
     */
    @Excel(name = "删除", readConverterExp = "0=否,1=是")
    private String isDelete;

    /**
     * 删除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "删除时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date deletedTime;


    /**
     * 对象转封装类
     *
     * @param pictureInfo PictureInfo实体对象
     * @return PictureInfoVo
     */
    public static PictureInfoVo objToVo(PictureInfo pictureInfo) {
        if (pictureInfo == null) {
            return null;
        }
        PictureInfoVo pictureInfoVo = new PictureInfoVo();
        BeanUtils.copyProperties(pictureInfo, pictureInfoVo);
        return pictureInfoVo;
    }
}
