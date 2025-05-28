package com.lz.user.model.dto.informInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.common.core.page.PageDomain;
import com.lz.user.model.domain.InformInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 用户通知记录Query对象 u_inform_info
 * 用户查询
 * @author YY
 * @date 2025-05-27
 */
@Data
public class UserInformInfoQuery extends PageDomain implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 模版类型（1短信 2邮件 3站内通知 4APP推送 5微信模板）
     */
    private String templateType;


    /**
     * 通知标题
     */
    private String informTitle;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 通知类型
     */
    private String informType;


    /**
     * 是否已读（0=未读 1=已读）
     */
    private String isRead;

    /**
     * 对象转封装类
     *
     * @param informInfoQuery 查询对象
     * @return InformInfo
     */
    public static InformInfo queryToObj(UserInformInfoQuery informInfoQuery) {
        if (informInfoQuery == null) {
            return null;
        }
        InformInfo informInfo = new InformInfo();
        BeanUtils.copyProperties(informInfoQuery, informInfo);
        return informInfo;
    }
}
