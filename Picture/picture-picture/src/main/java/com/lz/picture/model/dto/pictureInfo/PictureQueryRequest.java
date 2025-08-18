package com.lz.picture.model.dto.pictureInfo;

import com.lz.picture.model.domain.PictureInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 图片查询
 * 加油加油
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-07-21  20:28
 * @Version: 1.0
 */
@Data
public class PictureQueryRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 图片名称
     */
    private String name;

    /**
     * 分类编号
     */
    private String categoryId;


    /**
     * 所属空间编号
     */
    private String spaceId;

    /**
     * 当前记录起始索引
     */
    private Integer pageNum;

    /**
     * 每页显示记录数
     */
    private Integer pageSize;

    /**
     * 排序列
     */
    private String orderByColumn;

    /**
     * 上传类型
     */
    private String uploadType;


    /**
     * 对象转封装类
     *
     * @param request 查询对象
     * @return PictureInfo
     */
    public static PictureInfo requestToObj(PictureQueryRequest request) {
        if (request == null) {
            return null;
        }
        PictureInfo pictureInfo = new PictureInfo();
        BeanUtils.copyProperties(request, pictureInfo);
        return pictureInfo;
    }
}
