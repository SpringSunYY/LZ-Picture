package com.lz.config.model.dto.i18nKeyInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.config.model.domain.I18nKeyInfo;
/**
 * 国际化键名Vo对象 c_i18n_key_info
 *
 * @author YY
 * @date 2025-02-28
 */
@Data
public class I18nKeyInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long keyId;

    /** 键 */
    private String keyName;

    /** 显示顺序 */
    private Long orderNum;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param i18nKeyInfoInsert 插入对象
     * @return I18nKeyInfoInsert
     */
    public static I18nKeyInfo insertToObj(I18nKeyInfoInsert i18nKeyInfoInsert) {
        if (i18nKeyInfoInsert == null) {
            return null;
        }
        I18nKeyInfo i18nKeyInfo = new I18nKeyInfo();
        BeanUtils.copyProperties(i18nKeyInfoInsert, i18nKeyInfo);
        return i18nKeyInfo;
    }
}
