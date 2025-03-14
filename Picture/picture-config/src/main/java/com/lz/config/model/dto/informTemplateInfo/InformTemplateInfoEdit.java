package com.lz.config.model.dto.informTemplateInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.config.model.domain.InformTemplateInfo;
/**
 * 通知模版Vo对象 c_inform_template_info
 *
 * @author ruoyi
 * @date 2025-03-14
 */
@Data
public class InformTemplateInfoEdit implements Serializable
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

    /** 内容 */
    private String content;

    /** 事例 */
    private String example;

    /** 变量列表 */
    private String variables;

    /** 模版样式图 */
    private String templateImage;

    /** 状态（0=待审核 1=已启用 2=已禁用 3=审核失败） */
    private String status;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param informTemplateInfoEdit 编辑对象
     * @return InformTemplateInfo
     */
    public static InformTemplateInfo editToObj(InformTemplateInfoEdit informTemplateInfoEdit) {
        if (informTemplateInfoEdit == null) {
            return null;
        }
        InformTemplateInfo informTemplateInfo = new InformTemplateInfo();
        BeanUtils.copyProperties(informTemplateInfoEdit, informTemplateInfo);
        return informTemplateInfo;
    }
}
