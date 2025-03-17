package com.lz.user.model.dto.informInfo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.user.model.domain.InformInfo;
/**
 * 用户通知记录Vo对象 u_inform_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class InformInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 通知记录 */
    private String recordId;

    /** 模板 */
    private Long templateId;

    /** 用户 */
    private String userId;

    /** 实际发送内容 */
    private String content;

    /** 通知类型 */
    private String informType;

    /** 发送状态（0=待发送 1=已发送 2=发送失败 3=已撤回） */
    private Integer status;

    /** 是否已读（0=未读 1=已读） */
    private Integer isRead;

    /** 读取时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date readTime;

    /** 发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendTime;

    /** 备注 */
    private String remark;

    /** 逻辑删除标记（0=正常 1=删除） */
    private Integer isDeleted;

    /**
     * 对象转封装类
     *
     * @param informInfoInsert 插入对象
     * @return InformInfoInsert
     */
    public static InformInfo insertToObj(InformInfoInsert informInfoInsert) {
        if (informInfoInsert == null) {
            return null;
        }
        InformInfo informInfo = new InformInfo();
        BeanUtils.copyProperties(informInfoInsert, informInfo);
        return informInfo;
    }
}
