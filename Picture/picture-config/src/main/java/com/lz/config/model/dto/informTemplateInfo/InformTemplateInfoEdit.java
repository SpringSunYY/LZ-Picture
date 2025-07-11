package com.lz.config.model.dto.informTemplateInfo;

import com.lz.common.annotation.Excel;
import com.lz.config.model.domain.InformTemplateInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 通知模版Vo对象 c_inform_template_info
 *
 * @author YY
 * @date 2025-03-15
 */
@Data
public class InformTemplateInfoEdit implements Serializable {
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
     * 模版KEY
     */
    private String templateKey;

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
     * 通知标题
     */
    private String informTitle;

    /**
     * 扩展配置
     */
    private String extendConfig;

    /**
     * 版本
     */
    private Long templateVersion;

    /**
     * 历史版本
     */
    private String templateVersionHistory;

    /**
     * 内容
     */
    private String content;

    /**
     * 事例
     */
    private String example;

    /**
     * 变量列表
     */
    private String variables;

    /**
     * 模版样式图
     */
    private String templateImage;

    /**
     * 状态（0=待审核 1=已启用 2=已禁用 3=审核失败）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


    /**
     * 保存版本
     */
    private Boolean saveVersion;

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
