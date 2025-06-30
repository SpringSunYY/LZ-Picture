package com.lz.picture.model.dto.spaceInvitationInfo;

import com.lz.picture.model.domain.SpaceInvitationInfo;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 空间成员邀请记录Vo对象 p_space_invitation_info
 * 用户添加邀请记录
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class SpaceInvitationInfoAdd implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 空间编号
     */
    @NotEmpty(message = "空间编号不能为空")
    private String spaceId;

    /**
     * 邀请角色（0创建者 1管理员 2编辑者 3浏览者）
     */
    @NotEmpty(message = "请选择邀请角色")
    private String roleType;

    /**
     * 邀请理由
     */
    @NotEmpty(message = "请填写邀请理由")
    @Size(max = 512, message = "请填写小于512个字符的邀请理由")
    private String invitation;

    /**
     * 被邀请用户编号
     */
    @NotEmpty(message = "请输入需要邀请的用户")
    private String userName;

    /**
     * 对象转封装类
     *
     * @param spaceInvitationInfoInsert 插入对象
     * @return SpaceInvitationInfoInsert
     */
    public static SpaceInvitationInfo insertToObj(SpaceInvitationInfoAdd spaceInvitationInfoInsert) {
        if (spaceInvitationInfoInsert == null) {
            return null;
        }
        SpaceInvitationInfo spaceInvitationInfo = new SpaceInvitationInfo();
        BeanUtils.copyProperties(spaceInvitationInfoInsert, spaceInvitationInfo);
        return spaceInvitationInfo;
    }
}
