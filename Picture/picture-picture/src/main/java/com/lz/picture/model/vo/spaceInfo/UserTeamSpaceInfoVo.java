package com.lz.picture.model.vo.spaceInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.domain.SpaceMemberInfo;
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
 * 用户页面展示vo 用户团队空间
 * Version: 1.0
 */
@Data
public class UserTeamSpaceInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 空间编号
     */
    private String spaceId;

    /**
     * 成员编号
     */
    private String memberId;

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
     * 成员上限
     */
    private Long memberLimit;
    /**
     * 空间人数
     */
    private Long currentMembers;
    /**
     * 用户编号
     */
    @Excel(name = "用户编号")
    private String userId;

    /**
     * 角色（0创建者 1管理员 2编辑者 3浏览者）
     */
    @Excel(name = "角色", readConverterExp = "0=创建者,1=编辑者,2=预览者")
    private String roleType;

    /**
     * 最后操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastActiveTime;

    /**
     * 加入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "加入时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 空间状态 字典类型：p_space_status
     */
    private String spaceStatus;

    /**
     * 最后上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    /**
     * 对象转封装类
     *
     * @param spaceInfo SpaceInfo实体对象
     * @return SpaceInfoVo
     */
    public static UserTeamSpaceInfoVo objToVo(SpaceInfo spaceInfo) {
        if (spaceInfo == null) {
            return null;
        }
        UserTeamSpaceInfoVo spaceInfoVo = new UserTeamSpaceInfoVo();
        BeanUtils.copyProperties(spaceInfo, spaceInfoVo);
        return spaceInfoVo;
    }

    /**
     * 封装类转对象 list列表
     */
    public static List<UserTeamSpaceInfoVo> objToVo(List<SpaceInfo> spaceInfoList) {
        return spaceInfoList.stream().map(UserTeamSpaceInfoVo::objToVo).toList();
    }

    public UserTeamSpaceInfoVo(SpaceMemberInfo spaceMemberInfo, SpaceInfo spaceInfo) {
        this.spaceId = spaceInfo.getSpaceId();
        this.spaceName = spaceInfo.getSpaceName();
        this.spaceAvatar = spaceInfo.getSpaceAvatar();
        this.ossType = spaceInfo.getOssType();
        this.maxCount = spaceInfo.getMaxCount();
        this.maxSize = spaceInfo.getMaxSize();
        this.totalCount = spaceInfo.getTotalCount();
        this.totalSize = spaceInfo.getTotalSize();
        this.spaceStatus = spaceInfo.getSpaceStatus();
        this.lastUpdateTime = spaceInfo.getLastUpdateTime();
        this.memberLimit = spaceInfo.getMemberLimit();
        this.memberId = spaceMemberInfo.getMemberId();
        this.userId = spaceMemberInfo.getUserId();
        this.roleType = spaceMemberInfo.getRoleType();
        this.lastActiveTime = spaceMemberInfo.getLastActiveTime();
        this.createTime = spaceMemberInfo.getCreateTime();
    }

    public UserTeamSpaceInfoVo() {
    }
}
