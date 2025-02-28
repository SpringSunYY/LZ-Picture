package com.lz.config.model.dto.i18nLocaleInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.config.model.domain.I18nLocaleInfo;
/**
 * 国际化国家Vo对象 c_i18n_locale_info
 *
 * @author ruoyi
 * @date 2025-02-28
 */
@Data
public class I18nLocaleInfoEdit implements Serializable
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
     * @param i18nLocaleInfoEdit 编辑对象
     * @return I18nLocaleInfo
     */
    public static I18nLocaleInfo editToObj(I18nLocaleInfoEdit i18nLocaleInfoEdit) {
        if (i18nLocaleInfoEdit == null) {
            return null;
        }
        I18nLocaleInfo i18nLocaleInfo = new I18nLocaleInfo();
        BeanUtils.copyProperties(i18nLocaleInfoEdit, i18nLocaleInfo);
        return i18nLocaleInfo;
    }
}
