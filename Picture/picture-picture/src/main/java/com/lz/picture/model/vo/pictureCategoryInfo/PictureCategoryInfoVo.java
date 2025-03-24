package com.lz.picture.model.vo.pictureCategoryInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureCategoryInfo;
/**
 * 图片分类信息Vo对象 p_picture_category_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureCategoryInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 分类编号 */
    @Excel(name = "分类编号")
    private String categoryId;

    /** 父级分类编号 */
    @Excel(name = "父级分类编号")
    private String parentId;

    /** 祖级列表 */
    @Excel(name = "祖级列表")
    private String ancestors;

    /** 封面图URL */
    @Excel(name = "封面图URL")
    private String coverUrl;

    /** 分类名称 */
    @Excel(name = "分类名称")
    private String name;

    /** 分类描述 */
    @Excel(name = "分类描述")
    private String categoryDesc;

    /** 分类状态（0正常 1关闭） */
    @Excel(name = "分类状态", readConverterExp = "0=正常,1=关闭")
    private String categoryStatus;

    /** 查询状态（0是 1否） */
    @Excel(name = "查询状态", readConverterExp = "0=是,1=否")
    private String queryStatus;

    /** 使用次数 */
    @Excel(name = "使用次数")
    private Long usageCount;

    /** 查看次数 */
    @Excel(name = "查看次数")
    private Long lookCount;

    /** 下载次数 */
    @Excel(name = "下载次数")
    private Long downloadCount;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 删除标记（0否 1是） */
    @Excel(name = "删除标记", readConverterExp = "0=否,1=是")
    private String isDelete;


     /**
     * 对象转封装类
     *
     * @param pictureCategoryInfo PictureCategoryInfo实体对象
     * @return PictureCategoryInfoVo
     */
    public static PictureCategoryInfoVo objToVo(PictureCategoryInfo pictureCategoryInfo) {
        if (pictureCategoryInfo == null) {
            return null;
        }
        PictureCategoryInfoVo pictureCategoryInfoVo = new PictureCategoryInfoVo();
        BeanUtils.copyProperties(pictureCategoryInfo, pictureCategoryInfoVo);
        return pictureCategoryInfoVo;
    }
}
