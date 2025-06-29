
package com.lz.picture.model.dto.spaceInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.common.core.page.PageDomain;
import com.lz.picture.model.domain.SpaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 空间信息Query对象 p_space_info
 * 用户空间查询
 *
 * @author YY
 * @date 2025-03-24
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class UserTeamSpaceInfoQuery extends PageDomain implements Serializable {
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
     * 用户编号
     */
    private String userId;

    /**
     * 角色（0创建者 1管理员 2编辑者 3浏览者）
     */
    private String roleType;


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
     * @param spaceInfoQuery 查询对象
     * @return SpaceInfo
     */
    public static SpaceInfo queryToObj(UserTeamSpaceInfoQuery spaceInfoQuery) {
        if (spaceInfoQuery == null) {
            return null;
        }
        SpaceInfo spaceInfo = new SpaceInfo();
        BeanUtils.copyProperties(spaceInfoQuery, spaceInfo);
        return spaceInfo;
    }
}
