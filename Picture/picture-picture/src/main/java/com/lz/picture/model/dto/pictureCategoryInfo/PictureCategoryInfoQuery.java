package com.lz.picture.model.dto.pictureCategoryInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureCategoryInfo;
/**
 * 图片分类信息Query对象 p_picture_category_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureCategoryInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 分类编号 */
    private String categoryId;

    /** 分类名称 */
    private String name;

    /** 分类状态（0正常 1关闭） */
    private String categoryStatus;

    /** 查询状态（0是 1否） */
    private String queryStatus;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 删除标记（0否 1是） */
    private String isDelete;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param pictureCategoryInfoQuery 查询对象
     * @return PictureCategoryInfo
     */
    public static PictureCategoryInfo queryToObj(PictureCategoryInfoQuery pictureCategoryInfoQuery) {
        if (pictureCategoryInfoQuery == null) {
            return null;
        }
        PictureCategoryInfo pictureCategoryInfo = new PictureCategoryInfo();
        BeanUtils.copyProperties(pictureCategoryInfoQuery, pictureCategoryInfo);
        return pictureCategoryInfo;
    }
}
