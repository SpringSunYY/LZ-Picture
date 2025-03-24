package com.lz.picture.model.dto.spaceMemberInfo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.SpaceMemberInfo;
/**
 * 空间成员信息Vo对象 p_space_member_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class SpaceMemberInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 成员编号 */
    private String memberId;

    /** 空间编号 */
    private String spaceId;

    /** 用户编号 */
    private String userId;

    /** 角色（0创建者 1管理员 2编辑者 3浏览者） */
    private String roleType;

    /** 最后操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastActiveTime;

    /** 邀请人编号 */
    private String inviterUserId;

    /** 加入方式（0邀请） */
    private String joinType;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param spaceMemberInfoEdit 编辑对象
     * @return SpaceMemberInfo
     */
    public static SpaceMemberInfo editToObj(SpaceMemberInfoEdit spaceMemberInfoEdit) {
        if (spaceMemberInfoEdit == null) {
            return null;
        }
        SpaceMemberInfo spaceMemberInfo = new SpaceMemberInfo();
        BeanUtils.copyProperties(spaceMemberInfoEdit, spaceMemberInfo);
        return spaceMemberInfo;
    }
}
