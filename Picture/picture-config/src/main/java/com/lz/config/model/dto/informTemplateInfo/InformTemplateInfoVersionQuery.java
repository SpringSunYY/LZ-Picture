package com.lz.config.model.dto.informTemplateInfo;

import com.lz.common.utils.StringUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.config.model.domain.InformTemplateInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Project: Picture
 * Package: com.lz.config.model.dto.informTemplateInfo
 * Author: YY
 * CreateTime: 2025-03-16  14:04
 * Description: InformTemplateInfoVersionQuery
 * 根据版本号查询通知模版
 * Version: 1.0
 */
@Data
public class InformTemplateInfoVersionQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long templateId;

    /**
     * 版本
     */
    private Long templateVersion;

    public static InformTemplateInfo queryToObj(InformTemplateInfoVersionQuery informTemplateInfoVersionQuery) {
        if (StringUtils.isNull(informTemplateInfoVersionQuery)) {
            return new InformTemplateInfo();
        }
        InformTemplateInfo informTemplateInfo = new InformTemplateInfo();
        BeanUtils.copyProperties(informTemplateInfoVersionQuery, informTemplateInfo);
        return informTemplateInfo;
    }
}
