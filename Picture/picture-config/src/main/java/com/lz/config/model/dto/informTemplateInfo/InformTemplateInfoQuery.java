package com.lz.config.model.dto.informTemplateInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.config.model.domain.InformTemplateInfo;
/**
 * 通知模版Query对象 c_inform_template_info
 *
 * @author ruoyi
 * @date 2025-03-14
 */
@Data
public class InformTemplateInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long templateId;

    /** 模版名称 */
    private String templateName;

    /** 语言（默认zh-CN） */
    private String locale;

    /** 模版类型（1=短信 2=邮件 3=站内通知 4=APP推送 5=微信模板） */
    private String templateType;

    /** 渠道 */
    private String channel;

    /** 状态（0=待审核 1=已启用 2=已禁用 3=审核失败） */
    private String status;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 请求参数 */
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
