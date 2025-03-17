package com.lz.config.model.dto.i18nMessageInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.config.model.domain.I18nMessageInfo;
/**
 * 国际化信息Vo对象 c_i18n_message_info
 *
 * @author YY
 * @date 2025-02-28
 */
@Data
public class I18nMessageInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long messageId;

    /** 键 */
    private String messageKey;

    /** 简称 */
    private String locale;

    /** 消息 */
    private String message;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param i18nMessageInfoInsert 插入对象
     * @return I18nMessageInfoInsert
     */
    public static I18nMessageInfo insertToObj(I18nMessageInfoInsert i18nMessageInfoInsert) {
        if (i18nMessageInfoInsert == null) {
            return null;
        }
        I18nMessageInfo i18nMessageInfo = new I18nMessageInfo();
        BeanUtils.copyProperties(i18nMessageInfoInsert, i18nMessageInfo);
        return i18nMessageInfo;
    }
}
