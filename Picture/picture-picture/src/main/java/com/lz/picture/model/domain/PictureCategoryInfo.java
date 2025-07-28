package com.lz.picture.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.common.annotation.Excel;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 图片分类信息对象 p_picture_category_info
 *
 * @author YY
 * @date 2025-04-08
 */
@TableName("p_picture_category_info")
@Data
public class PictureCategoryInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类编号
     */
    @Excel(name = "分类编号")
    @TableId(value = "category_id", type = IdType.ASSIGN_ID)
    private String categoryId;

    /**
     * 父级分类编号
     */
    @Excel(name = "父级分类编号")
    private String parentId;

    /**
     * 祖级列表
     */
    @Excel(name = "祖级列表")
    private String ancestors;

    /**
     * 封面图URL
     */
    @Excel(name = "封面图URL")
    private String coverUrl;

    /**
     * 封面图标
     */
    @Excel(name = "封面图标")
    private String categoryIcon;


    /**
     * 分类名称
     */
    @Excel(name = "分类名称")
    private String name;

    /**
     * 显示顺序
     */
    @Excel(name = "显示顺序")
    private Long orderNum;

    /**
     * 分类描述
     */
    @Excel(name = "分类描述")
    private String categoryDesc;

    /**
     * 分类状态（0正常 1关闭）
     */
    @Excel(name = "分类状态", readConverterExp = "0=正常,1=关闭")
    private String categoryStatus;

    /**
     * 分类类型
     */
    @Excel(name = "分类类型")
    private String categoryType;

    /**
     * 查询状态（0是 1否）
     */
    @Excel(name = "查询状态", readConverterExp = "0=是,1=否")
    private String queryStatus;

    /**
     * 使用次数
     */
    @Excel(name = "使用次数")
    private Long usageCount;

    /**
     * 查看次数
     */
    @Excel(name = "查看次数")
    private Long lookCount;

    /**
     * 下载次数
     */
    @Excel(name = "下载次数")
    private Long downloadCount;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 删除标记（0否 1是）
     */
    @Excel(name = "删除标记", readConverterExp = "0=否,1=是")
    private String isDelete;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
