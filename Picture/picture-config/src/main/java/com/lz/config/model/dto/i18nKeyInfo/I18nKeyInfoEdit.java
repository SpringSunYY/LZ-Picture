package com.lz.config.model.dto.i18nKeyInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.config.model.domain.I18nKeyInfo;
/**
 * 国际化键名Vo对象 c_i18n_key_info
 *
 * @author ruoyi
 * @date 2025-02-28
 */
@Data
public class I18nKeyInfoEdit implements Serializable
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
     * @param i18nKeyInfoEdit 编辑对象
     * @return I18nKeyInfo
     */
    public static I18nKeyInfo editToObj(I18nKeyInfoEdit i18nKeyInfoEdit) {
        if (i18nKeyInfoEdit == null) {
            return null;
        }
        I18nKeyInfo i18nKeyInfo = new I18nKeyInfo();
        BeanUtils.copyProperties(i18nKeyInfoEdit, i18nKeyInfo);
        return i18nKeyInfo;
    }
}
