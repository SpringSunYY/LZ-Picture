package com.lz.picture.model.dto.spaceMemberInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.common.core.page.PageDomain;
import com.lz.picture.model.domain.SpaceMemberInfo;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 空间成员信息Query对象 p_space_member_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class UserSpaceMemberInfoQuery extends PageDomain implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 空间编号
     */
    @NotEmpty(message = "空间编号不能为空")
    private String spaceId;

    /**
     * 角色（0创建者 1管理员 2编辑者 3浏览者）
     */
    private String roleType;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 最后操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastActiveTime;

    /**
     * 加入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;


    /**
     * 加入方式（0邀请）
     */
    private String joinType;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param spaceMemberInfoQuery 查询对象
     * @return SpaceMemberInfo
     */
    public static SpaceMemberInfo queryToObj(UserSpaceMemberInfoQuery spaceMemberInfoQuery) {
        if (spaceMemberInfoQuery == null) {
            return null;
        }
        SpaceMemberInfo spaceMemberInfo = new SpaceMemberInfo();
        BeanUtils.copyProperties(spaceMemberInfoQuery, spaceMemberInfo);
        return spaceMemberInfo;
    }
}
