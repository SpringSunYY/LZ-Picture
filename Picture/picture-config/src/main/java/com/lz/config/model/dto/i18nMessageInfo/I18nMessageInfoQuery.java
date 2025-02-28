package com.lz.config.model.dto.i18nMessageInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.config.model.domain.I18nMessageInfo;
/**
 * 国际化信息Query对象 c_i18n_message_info
 *
 * @author ruoyi
 * @date 2025-02-28
 */
@Data
public class I18nMessageInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 键 */
    private String messageKey;

    /** 简称 */
    private String locale;

    /** 消息 */
    private String message;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param i18nMessageInfoQuery 查询对象
     * @return I18nMessageInfo
     */
    public static I18nMessageInfo queryToObj(I18nMessageInfoQuery i18nMessageInfoQuery) {
        if (i18nMessageInfoQuery == null) {
            return null;
        }
        I18nMessageInfo i18nMessageInfo = new I18nMessageInfo();
        BeanUtils.copyProperties(i18nMessageInfoQuery, i18nMessageInfo);
        return i18nMessageInfo;
    }
}
