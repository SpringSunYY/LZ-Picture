package com.lz.picture.model.vo.spaceInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.SpaceInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Project: Picture
 * Package: com.lz.picture.model.vo.spaceInfo
 * Author: YY
 * CreateTime: 2025-04-02  09:11
 * Description: UserSpaceInfoVo
 * 用户页面展示vo 用户个人空间
 * Version: 1.0
 */
@Data
public class UserPersonalSpaceInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 空间编号
     */
    private String spaceId;

    /**
     * 空间名称
     */
    private String spaceName;

    /**
     * 空间封面URL
     */
    private String spaceAvatar;

    /**
     * 存储类型（0官方 阿里云） 字典类型：p_space_oss_type
     */
    private String ossType;


    /**
     * 最大容量（字节）
     */
    private Long maxSize;

    /**
     * 最大文件数
     */
    private Long maxCount;

    /**
     * 已用容量（字节）
     */
    private Long totalSize;

    /**
     * 文件总数
     */
    private Long totalCount;

    /**
     * 空间描述
     */
    private String spaceDesc;

    /**
     * 空间状态 字典类型：p_space_status
     */
    private String spaceStatus;

    /**
     * 空间类型（0个人 1团队 2官方）字典类型：p_space_type
     */
    private String spaceType;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 最后上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    /**
     * 最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 对象转封装类
     *
     * @param spaceInfo SpaceInfo实体对象
     * @return SpaceInfoVo
     */
    public static UserPersonalSpaceInfoVo objToVo(SpaceInfo spaceInfo) {
        if (spaceInfo == null) {
            return null;
        }
        UserPersonalSpaceInfoVo spaceInfoVo = new UserPersonalSpaceInfoVo();
        BeanUtils.copyProperties(spaceInfo, spaceInfoVo);
        return spaceInfoVo;
    }

    /**
     * 封装类转对象 list列表
     */
    public static List<UserPersonalSpaceInfoVo> objToVo(List<SpaceInfo> spaceInfoList) {
        return spaceInfoList.stream().map(UserPersonalSpaceInfoVo::objToVo).toList();
    }
}
