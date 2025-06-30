package com.lz.picture.model.dto.spaceInvitationInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.picture.model.domain.SpaceInvitationInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
/**
 * 空间成员邀请记录Query对象 p_space_invitation_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class SpaceInvitationInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 邀请编号 */
    private String invitationId;

    /** 空间编号 */
    private String spaceId;

    /** 空间名称 */
    private String spaceName;

    /** 邀请角色（0创建者 1管理员 2编辑者 3浏览者） */
    private String roleType;

    /** 邀请状态（0待同意 1同意 2拒绝 3过期） */
    private String invitationStatus;

    /** 邀请人编号 */
    private String invitationUserId;

    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireTime;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 被邀请用户编号 */
    private String userId;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param spaceInvitationInfoQuery 查询对象
     * @return SpaceInvitationInfo
     */
    public static SpaceInvitationInfo queryToObj(SpaceInvitationInfoQuery spaceInvitationInfoQuery) {
        if (spaceInvitationInfoQuery == null) {
            return null;
        }
        SpaceInvitationInfo spaceInvitationInfo = new SpaceInvitationInfo();
        BeanUtils.copyProperties(spaceInvitationInfoQuery, spaceInvitationInfo);
        return spaceInvitationInfo;
    }
}
