package com.lz.config.model.vo.informTemplateInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.config.model.domain.InformTemplateInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 通知模版Vo对象 c_inform_template_info
 *
 * @author YY
 * @date 2025-03-15
 */
@Data
public class InformTemplateInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Excel(name = "主键")
    private Long templateId;

    /**
     * 模版名称
     */
    @Excel(name = "模版名称")
    private String templateName;

    /**
     * 模版KEY
     */
    @Excel(name = "模版KEY")
    private String templateKey;

    /**
     * 语言
     */
    @Excel(name = "语言")
    private String locale;

    /**
     * 渠道
     */
    @Excel(name = "渠道")
    private String channel;

    /**
     * 模版类型（1短信 2邮件 3站内通知 4APP推送 5微信模板）
     */
    @Excel(name = "模版类型", readConverterExp = "1=短信,2=邮件,3=站内通知,4=APP推送,5=微信模板")
    private String templateType;

    /**
     * 服务商模版ID
     */
    @Excel(name = "服务商模版ID")
    private String serviceTemplateId;

    /**
     * 服务商签名
     */
    @Excel(name = "服务商签名")
    private String serviceSignName;

    /**
     * 扩展配置
     */
    @Excel(name = "扩展配置")
    private String extendConfig;

    /**
     * 版本
     */
    @Excel(name = "版本")
    private Long templateVersion;

    /**
     * 历史版本
     */
    @Excel(name = "历史版本")
    private String templateVersionHistory;

    /**
     * 内容
     */
    @Excel(name = "内容")
    private String content;

    /**
     * 事例
     */
    @Excel(name = "事例")
    private String example;

    /**
     * 变量列表
     */
    @Excel(name = "变量列表")
    private String variables;

    /**
     * 模版样式图
     */
    @Excel(name = "模版样式图")
    private String templateImage;

    /**
     * 状态（0=待审核 1=已启用 2=已禁用 3=审核失败）
     */
    @Excel(name = "状态", readConverterExp = "0==待审核,1==已启用,2==已禁用,3==审核失败")
    private String status;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    @Excel(name = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;


    /**
     * 对象转封装类
     *
     * @param informTemplateInfo InformTemplateInfo实体对象
     * @return InformTemplateInfoVo
     */
    public static InformTemplateInfoVo objToVo(InformTemplateInfo informTemplateInfo) {
        if (informTemplateInfo == null) {
            return null;
        }
        InformTemplateInfoVo informTemplateInfoVo = new InformTemplateInfoVo();
        BeanUtils.copyProperties(informTemplateInfo, informTemplateInfoVo);
        return informTemplateInfoVo;
    }
}
