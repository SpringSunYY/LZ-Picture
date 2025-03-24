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
 * 用户浏览记录对象 p_user_view_log_info
 *
 * @author YY
 * @date 2025-03-24
 */
@TableName("p_user_view_log_info")
@Data
public class UserViewLogInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 浏览记录编号 */
        @Excel(name = "浏览记录编号")
    @TableId(value = "view_id", type = IdType.ASSIGN_ID)
    private String viewId;

    /** 用户编号 */
        @Excel(name = "用户编号")
    private String userId;

    /** 目标类型（0图片 1用户 2空间） */
        @Excel(name = "目标类型", readConverterExp = "0=图片,1=用户,2=空间")
    private String targetType;

    /** 目标对象编号 */
        @Excel(name = "目标对象编号")
    private String targetId;

    /** 图片分类 */
        @Excel(name = "图片分类")
    private String categoryId;

    /** 图片标签 */
        @Excel(name = "图片标签")
    private String tags;

    /** 封面URL */
        @Excel(name = "封面URL")
    private String targetCover;

    /** 查看时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "查看时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
