package com.lz.picture.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 图片信息对象 p_picture_info
 *
 * @author YY
 * @date 2025-03-25
 */
@TableName("p_picture_info")
@Data
public class PictureInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 图片编号
     */
    @Excel(name = "图片编号")
    @TableId(value = "picture_id", type = IdType.ASSIGN_ID)
    private String pictureId;

    /**
     * 图片URL
     */
    @Excel(name = "图片URL")
    private String pictureUrl;

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
     * 图片体积（字节）
     */
    @Excel(name = "图片体积", readConverterExp = "字=节")
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

    @TableField(exist = false)
    private List<String> tags;

    /**
     * 所属文件夹编号
     */
    @Excel(name = "所属文件夹编号")
    private String folderId;

    /**
     * 图片主色调（十六进制代码）
     */
    @Excel(name = "图片主色调", readConverterExp = "十=六进制代码")
    private String picColor;

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
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
