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
 * 用户图片推荐模型对象 p_picture_recommend_info
 *
 * @author YY
 * @date 2025-06-10
 */
@TableName("p_picture_recommend_info")
@Data
public class PictureRecommendInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 推荐编号
     */
    @Excel(name = "推荐编号")
    @TableId(value = "recommend_id", type = IdType.ASSIGN_ID)
    private String recommendId;

    /**
     * 分类分数
     */
    @Excel(name = "分类分数")
    private String categoryScores;

    /**
     * 高兴趣分类
     */
    @Excel(name = "高兴趣分类")
    private String topCategories;

    /**
     * 归一化分类分数
     */
    @Excel(name = "归一化分类分数")
    private String normalizedCategoryScores;

    /**
     * 标签分数
     */
    @Excel(name = "标签分数")
    private String tagScores;

    /**
     * 高兴趣标签
     */
    @Excel(name = "高兴趣标签")
    private String topTags;

    /**
     * 归一化标签分数
     */
    @Excel(name = "归一化标签分数")
    private String normalizedTagScores;

    /**
     * 更多信息
     */
    @Excel(name = "更多信息")
    private String moreInfo;

    /**
     * 用户编号
     */
    @Excel(name = "用户编号")
    private String userId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
