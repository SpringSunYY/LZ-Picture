package com.lz.picture.model.dto.spaceInvitationInfo;

import com.lz.common.core.page.PageDomain;
import com.lz.picture.model.domain.SpaceInvitationInfo;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 空间成员邀请记录Query对象 p_space_invitation_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class UserSpaceInvitationInfoAction extends PageDomain implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 邀请编号
     */
    @NotEmpty(message = "请选择邀请编号")
    private String invitationId;

    /**
     * 邀请状态（0待同意 1同意 2拒绝 3过期）
     */
    @NotEmpty(message = "请选择邀请状态")
    private String invitationStatus;


    /**
     * 对象转封装类
     *
     * @param spaceInvitationInfoQuery 查询对象
     * @return SpaceInvitationInfo
     */
    public static SpaceInvitationInfo dtoToObj(UserSpaceInvitationInfoAction spaceInvitationInfoQuery) {
        if (spaceInvitationInfoQuery == null) {
            return null;
        }
        SpaceInvitationInfo spaceInvitationInfo = new SpaceInvitationInfo();
        BeanUtils.copyProperties(spaceInvitationInfoQuery, spaceInvitationInfo);
        return spaceInvitationInfo;
    }
}
