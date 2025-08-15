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
 * 用户页面展示vo
 * Version: 1.0
 */
@Data
public class UserSpaceInfoVo implements Serializable {
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
     * 所属用户
     */
    private String userId;

    /**
     * 空间描述
     */
    private String spaceDesc;

    /**
     * 空间状态
     */
    private String spaceStatus;

    /**
     * 空间类型（0个人 1团队 2官方）
     */
    private String spaceType;

    /**
     * 成员上限
     */
    private Long memberLimit;

    /**
     * 当前成员数
     */
    private Long currentMembers;

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
    public static UserSpaceInfoVo objToVo(SpaceInfo spaceInfo) {
        if (spaceInfo == null) {
            return null;
        }
        UserSpaceInfoVo spaceInfoVo = new UserSpaceInfoVo();
        BeanUtils.copyProperties(spaceInfo, spaceInfoVo);
        return spaceInfoVo;
    }

    public static List<UserSpaceInfoVo> objToVo(List<SpaceInfo> spaceInfos) {
        return spaceInfos.stream().map(UserSpaceInfoVo::objToVo).toList();
    }
}
