package com.lz.user.model.vo.userRelationInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.user.model.domain.UserRelationInfo;
/**
 * 用户关系Vo对象 u_user_relation_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class UserRelationInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 关系ID */
    @Excel(name = "关系ID")
    private String relationId;

    /** 用户 */
    @Excel(name = "用户")
    private String userId;

    /** 关联用户 */
    @Excel(name = "关联用户")
    private String relationUserId;

    /** 关系类型（0=关注 1=互关 2=拉黑） */
    @Excel(name = "关系类型", readConverterExp = "0==关注,1==互关,2==拉黑")
    private String relationType;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


     /**
     * 对象转封装类
     *
     * @param userRelationInfo UserRelationInfo实体对象
     * @return UserRelationInfoVo
     */
    public static UserRelationInfoVo objToVo(UserRelationInfo userRelationInfo) {
        if (userRelationInfo == null) {
            return null;
        }
        UserRelationInfoVo userRelationInfoVo = new UserRelationInfoVo();
        BeanUtils.copyProperties(userRelationInfo, userRelationInfoVo);
        return userRelationInfoVo;
    }
}
