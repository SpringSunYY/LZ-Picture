package com.lz.config.model.dto.i18nKeyInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.config.model.domain.I18nKeyInfo;
/**
 * 国际化键名Query对象 c_i18n_key_info
 *
 * @author ruoyi
 * @date 2025-02-28
 */
@Data
public class I18nKeyInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 键 */
    private String keyName;

    /** 显示顺序 */
    private Long orderNum;

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
     * @param i18nKeyInfoQuery 查询对象
     * @return I18nKeyInfo
     */
    public static I18nKeyInfo queryToObj(I18nKeyInfoQuery i18nKeyInfoQuery) {
        if (i18nKeyInfoQuery == null) {
            return null;
        }
        I18nKeyInfo i18nKeyInfo = new I18nKeyInfo();
        BeanUtils.copyProperties(i18nKeyInfoQuery, i18nKeyInfo);
        return i18nKeyInfo;
    }
}
