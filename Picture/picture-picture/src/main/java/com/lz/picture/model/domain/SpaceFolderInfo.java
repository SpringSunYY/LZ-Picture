package com.lz.picture.model.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.lz.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 空间文件夹对象 p_space_folder_info
 *
 * @author YY
 * @date 2025-03-24
 */
@TableName("p_space_folder_info")
@Data
public class SpaceFolderInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 文件夹编号
     */
    @Excel(name = "文件夹编号")
    @TableId(value = "folder_id", type = IdType.ASSIGN_ID)
    private String folderId;

    /**
     * 空间编号
     */
    @Excel(name = "空间编号")
    private String spaceId;

    /**
     * 父文件夹编号
     */
    @Excel(name = "父文件夹编号")
    private String parentId;

    /**
     * 祖级列表
     */
    @Excel(name = "祖级列表")
    private String ancestors;

    /**
     * 文件夹名称
     */
    @Excel(name = "文件夹名称")
    private String folderName;

    /**
     * 完整路径（格式：/文件夹名1/文件夹名2/）
     */
    @Excel(name = "完整路径", readConverterExp = "格=式：/文件夹名1/文件夹名2/")
    private String fullPath;

    /**
     * 层级
     */
    @Excel(name = "层级")
    private String folderLevel;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    private String userId;

    /**
     * 排序权重
     */
    @Excel(name = "排序权重")
    private Integer sortOrder;

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
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
