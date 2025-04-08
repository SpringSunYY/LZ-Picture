package com.lz.picture.model.vo.pictureTagInfo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureTagInfo;

/**
 * 图片标签信息Vo对象 p_picture_tag_info
 *
 * @author YY
 * @date 2025-04-09
 */
@Data
public class PictureTagInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标签编号
     */
    @Excel(name = "标签编号")
    private String tagId;

    /**
     * 标签名称
     */
    @Excel(name = "标签名称")
    private String name;

    /**
     * 标签状态
     */
    @Excel(name = "标签状态")
    private String tagsStatus;

    /**
     * 标签描述
     */
    @Excel(name = "标签描述")
    private String tagDesc;

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
     * 所属用户
     */
    @Excel(name = "所属用户")
    private String userId;

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
     * 对象转封装类
     *
     * @param pictureTagInfo PictureTagInfo实体对象
     * @return PictureTagInfoVo
     */
    public static PictureTagInfoVo objToVo(PictureTagInfo pictureTagInfo) {
        if (pictureTagInfo == null) {
            return null;
        }
        PictureTagInfoVo pictureTagInfoVo = new PictureTagInfoVo();
        BeanUtils.copyProperties(pictureTagInfo, pictureTagInfoVo);
        return pictureTagInfoVo;
    }
}
