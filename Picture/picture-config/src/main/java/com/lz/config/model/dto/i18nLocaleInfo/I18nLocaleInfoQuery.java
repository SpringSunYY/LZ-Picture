package com.lz.config.model.dto.i18nLocaleInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.config.model.domain.I18nLocaleInfo;
/**
 * 国际化国家Query对象 c_i18n_locale_info
 *
 * @author YY
 * @date 2025-02-28
 */
@Data
public class I18nLocaleInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 国家地区 */
    private String localeName;

    /** 简称 */
    private String locale;

    /** 状态（0正常 1隐藏） */
    private String localeStatus;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param i18nLocaleInfoQuery 查询对象
     * @return I18nLocaleInfo
     */
    public static I18nLocaleInfo queryToObj(I18nLocaleInfoQuery i18nLocaleInfoQuery) {
        if (i18nLocaleInfoQuery == null) {
            return null;
        }
        I18nLocaleInfo i18nLocaleInfo = new I18nLocaleInfo();
        BeanUtils.copyProperties(i18nLocaleInfoQuery, i18nLocaleInfo);
        return i18nLocaleInfo;
    }
}
