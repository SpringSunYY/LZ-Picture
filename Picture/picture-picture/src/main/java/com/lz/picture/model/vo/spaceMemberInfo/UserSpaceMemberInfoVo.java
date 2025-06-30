package com.lz.picture.model.vo.spaceMemberInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.picture.model.domain.SpaceMemberInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 空间成员信息Vo对象 p_space_member_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class UserSpaceMemberInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 成员编号
     */
    private String memberId;

    /**
     * 空间编号
     */
    private String spaceId;

    /**
     * 空间名称
     */
    private String spaceName;

    /**
     * 用户编号
     */
    private String userName;

    /**
     * 角色（0创建者 1管理员 2编辑者 3浏览者） 数据字典：p_space_role
     */
    private String roleType;

    /**
     * 最后操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastActiveTime;

    /**
     * 加入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 邀请人编号
     */
    private String inviterUserName;

    /**
     * 加入方式（0邀请）数据字典：p_space_join_type
     */
    private String joinType;

    /**
     * 对象转封装类
     *
     * @param spaceMemberInfo SpaceMemberInfo实体对象
     * @return SpaceMemberInfoVo
     */
    public static UserSpaceMemberInfoVo objToVo(SpaceMemberInfo spaceMemberInfo) {
        if (spaceMemberInfo == null) {
            return null;
        }
        UserSpaceMemberInfoVo spaceMemberInfoVo = new UserSpaceMemberInfoVo();
        BeanUtils.copyProperties(spaceMemberInfo, spaceMemberInfoVo);
        return spaceMemberInfoVo;
    }
}
