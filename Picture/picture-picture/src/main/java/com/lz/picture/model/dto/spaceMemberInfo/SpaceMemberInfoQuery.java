package com.lz.picture.model.dto.spaceMemberInfo;

import java.util.Map;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.SpaceMemberInfo;
/**
 * 空间成员信息Query对象 p_space_member_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class SpaceMemberInfoQuery implements Serializable
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

    /** 加入时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 邀请人编号 */
    private String inviterUserId;

    /** 加入方式（0邀请） */
    private String joinType;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param spaceMemberInfoQuery 查询对象
     * @return SpaceMemberInfo
     */
    public static SpaceMemberInfo queryToObj(SpaceMemberInfoQuery spaceMemberInfoQuery) {
        if (spaceMemberInfoQuery == null) {
            return null;
        }
        SpaceMemberInfo spaceMemberInfo = new SpaceMemberInfo();
        BeanUtils.copyProperties(spaceMemberInfoQuery, spaceMemberInfo);
        return spaceMemberInfo;
    }
}
