package com.lz.config.model.dto.informTemplateInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.config.model.domain.InformTemplateInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 通知模版Query对象 c_inform_template_info
 *
 * @author YY
 * @date 2025-03-15
 */
@Data
public class InformTemplateInfoQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long templateId;

    /**
     * 模版名称
     */
    private String templateName;

    /**
     * 语言
     */
    private String locale;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 模版类型（1短信 2邮件 3站内通知 4APP推送 5微信模板）
     */
    private String templateType;

    /**
     * 服务商模版ID
     */
    private String serviceTemplateId;

    /**
     * 服务商签名
     */
    private String serviceSignName;

    /**
     * 状态（0=待审核 1=已启用 2=已禁用 3=审核失败）
     */
    private String status;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param informTemplateInfoQuery 查询对象
     * @return InformTemplateInfo
     */
    public static InformTemplateInfo queryToObj(InformTemplateInfoQuery informTemplateInfoQuery) {
        if (informTemplateInfoQuery == null) {
            return null;
        }
        InformTemplateInfo informTemplateInfo = new InformTemplateInfo();
        BeanUtils.copyProperties(informTemplateInfoQuery, informTemplateInfo);
        return informTemplateInfo;
    }
}
