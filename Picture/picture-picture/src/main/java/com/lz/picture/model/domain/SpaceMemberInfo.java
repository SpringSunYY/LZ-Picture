package com.lz.picture.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.common.annotation.Excel;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 空间成员信息对象 p_space_member_info
 *
 * @author YY
 * @date 2025-03-24
 */
@TableName("p_space_member_info")
@Data
public class SpaceMemberInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 成员编号
     */
    @Excel(name = "成员编号")
    @TableId(value = "member_id", type = IdType.ASSIGN_ID)
    private String memberId;

    /**
     * 空间编号
     */
    @Excel(name = "空间编号")
    private String spaceId;

    /**
     * 用户编号
     */
    @Excel(name = "用户编号")
    private String userId;

    /**
     * 角色（0创建者 1管理员 2编辑者 3浏览者）
     */
    @Excel(name = "角色", readConverterExp = "0=创建者,1=管理员,2=编辑者,3=浏览者")
    private String roleType;

    /**
     * 最后操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastActiveTime;

    /**
     * 加入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "加入时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 邀请人编号
     */
    @Excel(name = "邀请人编号")
    private String inviterUserId;

    /**
     * 加入方式（0邀请）
     */
    @Excel(name = "加入方式", readConverterExp = "0=邀请")
    private String joinType;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
