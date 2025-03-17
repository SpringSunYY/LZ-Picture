package com.lz.user.model.dto.userRelationInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.user.model.domain.UserRelationInfo;
/**
 * 用户关系Query对象 u_user_relation_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class UserRelationInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 关系ID */
    private String relationId;

    /** 用户 */
    private String userId;

    /** 关联用户 */
    private String relationUserId;

    /** 关系类型（0=关注 1=互关 2=拉黑） */
    private String relationType;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param userRelationInfoQuery 查询对象
     * @return UserRelationInfo
     */
    public static UserRelationInfo queryToObj(UserRelationInfoQuery userRelationInfoQuery) {
        if (userRelationInfoQuery == null) {
            return null;
        }
        UserRelationInfo userRelationInfo = new UserRelationInfo();
        BeanUtils.copyProperties(userRelationInfoQuery, userRelationInfo);
        return userRelationInfo;
    }
}
