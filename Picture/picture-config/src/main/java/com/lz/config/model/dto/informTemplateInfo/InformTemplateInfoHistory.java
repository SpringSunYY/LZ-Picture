package com.lz.config.model.dto.informTemplateInfo;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.config.model.domain.InformTemplateInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Project: Picture
 * Package: com.lz.config.model.dto.informTemplateInfo
 * Author: YY
 * CreateTime: 2025-03-15  17:26
 * Description: InformTemplateInfoHistory
 * 历史版本
 * Version: 1.0
 */
@Data
public class InformTemplateInfoHistory implements Serializable {
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
     * 扩展配置
     */
    private String extendConfig;

    /**
     * 版本
     */
    private Long templateVersion;

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
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * description: 对象转历史记录
     * author: YY
     * method: objToHistory
     * date: 2025/3/15 17:31
     * param:
     * param: informTemplateInfo
     * return: com.lz.config.model.dto.informTemplateInfo.InformTemplateInfoHistory
     **/
    public static InformTemplateInfoHistory objToHistory(InformTemplateInfo informTemplateInfo) {
        if (StringUtils.isNull(informTemplateInfo)) {
            return new InformTemplateInfoHistory();
        }
        InformTemplateInfoHistory informTemplateInfoHistory = new InformTemplateInfoHistory();
        BeanUtils.copyProperties(informTemplateInfo, informTemplateInfoHistory);
        return informTemplateInfoHistory;
    }

    /**
     * description: 历史记录转为对象
     * author: YY
     * method: historyToObj
     * date: 2025/3/15 17:32
     * param:
     * param: informTemplateInfoHistory
     * return: com.lz.config.model.domain.InformTemplateInfo
     **/
    public static InformTemplateInfo historyToObj(InformTemplateInfoHistory informTemplateInfoHistory) {
        if (StringUtils.isNull(informTemplateInfoHistory)) {
            return new InformTemplateInfo();
        }
        InformTemplateInfo informTemplateInfo = new InformTemplateInfo();
        BeanUtils.copyProperties(informTemplateInfoHistory, informTemplateInfo);
        return informTemplateInfo;
    }

    /**
     * description:  根据历史记录获取模版信息
     * author: YY
     * method: getInformTemplateInfoByVersion
     * date: 2025/3/16 14:15
     * param:
     * param: informTemplateInfoHistory 历史版本json
     * return: com.lz.config.model.domain.InformTemplateInfo
     **/
    public static InformTemplateInfo getInformTemplateInfoByVersion(String informTemplateInfoHistory) {
        if (StringUtils.isEmpty(informTemplateInfoHistory)) {
            return new InformTemplateInfo();
        }
        InformTemplateInfoHistory history = JSONObject.parseObject(informTemplateInfoHistory, InformTemplateInfoHistory.class);
        return historyToObj(history);
    }
}
