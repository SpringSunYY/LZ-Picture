package com.lz.user.model.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.lz.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
/**
 * 用户关系对象 u_user_relation_info
 *
 * @author YY
 * @date 2025-03-17
 */
@TableName("u_user_relation_info")
@Data
public class UserRelationInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 关系ID */
        @Excel(name = "关系ID")
    @TableId(value = "relation_id", type = IdType.ASSIGN_ID)
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

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
