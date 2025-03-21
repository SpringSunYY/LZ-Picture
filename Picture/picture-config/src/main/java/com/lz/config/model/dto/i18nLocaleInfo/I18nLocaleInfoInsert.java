package com.lz.config.model.dto.i18nLocaleInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.config.model.domain.I18nLocaleInfo;
/**
 * 国际化国家Vo对象 c_i18n_locale_info
 *
 * @author YY
 * @date 2025-02-28
 */
@Data
public class I18nLocaleInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long localeId;

    /** 国家地区 */
    private String localeName;

    /** 简称 */
    private String locale;

    /** 状态（0正常 1隐藏） */
    private String localeStatus;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param i18nLocaleInfoInsert 插入对象
     * @return I18nLocaleInfoInsert
     */
    public static I18nLocaleInfo insertToObj(I18nLocaleInfoInsert i18nLocaleInfoInsert) {
        if (i18nLocaleInfoInsert == null) {
            return null;
        }
        I18nLocaleInfo i18nLocaleInfo = new I18nLocaleInfo();
        BeanUtils.copyProperties(i18nLocaleInfoInsert, i18nLocaleInfo);
        return i18nLocaleInfo;
    }
}
