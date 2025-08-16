package com.lz.picture.model.vo.spaceInfo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.SpaceInfo;

/**
 * 空间信息Vo对象 p_space_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class SpaceInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 空间编号
     */
    @Excel(name = "空间编号")
    private String spaceId;

    /**
     * 空间名称
     */
    @Excel(name = "空间名称")
    private String spaceName;

    /**
     * 空间封面URL
     */
    @Excel(name = "空间封面URL")
    private String spaceAvatar;


    /**
     * 最大容量（字节）
     */
    @Excel(name = "最大容量", readConverterExp = "字=节")
    private Long maxSize;

    /**
     * 最大文件数
     */
    @Excel(name = "最大文件数")
    private Long maxCount;

    /**
     * 查看次数
     */
    @Excel(name = "查看次数")
    private Long lookCount;

    /**
     * 收藏次数
     */
    @Excel(name = "收藏次数")
    private Long collectCount;

    /**
     * 下载次数
     */
    @Excel(name = "下载次数")
    private Long downloadCount;

    /**
     * 已用容量（字节）
     */
    @Excel(name = "已用容量", readConverterExp = "字=节")
    private Long totalSize;

    /**
     * 文件总数
     */
    @Excel(name = "文件总数")
    private Long totalCount;

    /**
     * 所属用户
     */
    @Excel(name = "所属用户")
    private String userId;

    /**
     * 空间描述
     */
    @Excel(name = "空间描述")
    private String spaceDesc;

    /**
     * 空间状态
     */
    @Excel(name = "空间状态")
    private String spaceStatus;

    /**
     * 空间类型（0个人 1团队 2官方）
     */
    @Excel(name = "空间类型", readConverterExp = "0=个人,1=团队,2=官方")
    private String spaceType;

    /**
     * 成员上限
     */
    @Excel(name = "成员上限")
    private Long memberLimit;

    /**
     * 当前成员数
     */
    @Excel(name = "当前成员数")
    private Long currentMembers;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 最后上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后上传时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    /**
     * 最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 删除（0否 1是）
     */
    @Excel(name = "删除", readConverterExp = "0=否,1=是")
    private String isDelete;

    /**
     * 删除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "删除时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date deletedTime;


    /**
     * 对象转封装类
     *
     * @param spaceInfo SpaceInfo实体对象
     * @return SpaceInfoVo
     */
    public static SpaceInfoVo objToVo(SpaceInfo spaceInfo) {
        if (spaceInfo == null) {
            return null;
        }
        SpaceInfoVo spaceInfoVo = new SpaceInfoVo();
        BeanUtils.copyProperties(spaceInfo, spaceInfoVo);
        return spaceInfoVo;
    }
}
