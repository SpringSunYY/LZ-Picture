package com.lz.picture.model.dto.spaceInvitationInfo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.SpaceInvitationInfo;
/**
 * 空间成员邀请记录Vo对象 p_space_invitation_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class SpaceInvitationInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 邀请编号 */
    private String invitationId;

    /** 空间编号 */
    private String spaceId;

    /** 空间名称 */
    private String spaceName;

    /** 空间封面URL */
    private String spaceAvatar;

    /** 邀请角色（0创建者 1管理员 2编辑者 3浏览者） */
    private String roleType;

    /** 邀请状态（0待同意 1同意 2拒绝 3过期） */
    private String invitationStatus;

    /** 邀请链接（短链或唯一标识） */
    private String invitationUrl;

    /** 邀请理由 */
    private String invitation;

    /** 邀请人编号 */
    private String invitationUserId;

    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireTime;

    /** 被邀请用户编号 */
    private String userId;

    /**
     * 对象转封装类
     *
     * @param spaceInvitationInfoEdit 编辑对象
     * @return SpaceInvitationInfo
     */
    public static SpaceInvitationInfo editToObj(SpaceInvitationInfoEdit spaceInvitationInfoEdit) {
        if (spaceInvitationInfoEdit == null) {
            return null;
        }
        SpaceInvitationInfo spaceInvitationInfo = new SpaceInvitationInfo();
        BeanUtils.copyProperties(spaceInvitationInfoEdit, spaceInvitationInfo);
        return spaceInvitationInfo;
    }
}
