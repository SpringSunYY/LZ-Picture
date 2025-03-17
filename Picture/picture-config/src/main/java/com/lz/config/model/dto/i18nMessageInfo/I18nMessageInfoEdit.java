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
public class I18nMessageInfoEdit implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long messageId;

    /**
     * 键
     */
    private String messageKey;

    /**
     * 简称
     */
    private String locale;

    /**
     * 消息
     */
    private String message;

    /**
     * 备注
     */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param i18nMessageInfoEdit 编辑对象
     * @return I18nMessageInfo
     */
    public static I18nMessageInfo editToObj(I18nMessageInfoEdit i18nMessageInfoEdit) {
        if (i18nMessageInfoEdit == null) {
            return null;
        }
        I18nMessageInfo i18nMessageInfo = new I18nMessageInfo();
        BeanUtils.copyProperties(i18nMessageInfoEdit, i18nMessageInfo);
        return i18nMessageInfo;
    }
}
