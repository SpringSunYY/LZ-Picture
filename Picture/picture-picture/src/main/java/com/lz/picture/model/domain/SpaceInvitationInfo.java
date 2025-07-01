package com.lz.picture.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 空间成员邀请记录对象 p_space_invitation_info
 *
 * @author YY
 * @date 2025-03-24
 */
@TableName("p_space_invitation_info")
@Data
public class SpaceInvitationInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 邀请编号
     */
    @Excel(name = "邀请编号")
    @TableId(value = "invitation_id", type = IdType.ASSIGN_ID)
    private String invitationId;

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
     * 邀请角色（0创建者 1管理员 2编辑者 3浏览者）
     */
    @Excel(name = "邀请角色", readConverterExp = "0=创建者,1=编辑者,2=预览者")
    private String roleType;

    /**
     * 邀请状态（0待同意 1同意 2拒绝 3过期）
     */
    @Excel(name = "邀请状态", readConverterExp = "0=待同意,1=同意,2=拒绝,3=过期")
    private String invitationStatus;

    /**
     * 邀请链接（短链或唯一标识）
     */
    @Excel(name = "邀请链接", readConverterExp = "短=链或唯一标识")
    private String invitationUrl;

    /**
     * 邀请理由
     */
    @Excel(name = "邀请理由")
    private String invitation;

    /**
     * 邀请人编号
     */
    @Excel(name = "邀请人编号")
    private String invitationUserId;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "过期时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 被邀请用户编号
     */
    @Excel(name = "被邀请用户编号")
    private String userId;
    @Excel(name = "被邀请用户")
    @TableField(exist = false)
    private String userName;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
