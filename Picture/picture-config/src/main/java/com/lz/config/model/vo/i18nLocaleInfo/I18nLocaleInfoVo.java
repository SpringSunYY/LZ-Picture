package com.lz.config.model.vo.i18nLocaleInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.config.model.domain.I18nLocaleInfo;
/**
 * 国际化国家Vo对象 c_i18n_locale_info
 *
 * @author YY
 * @date 2025-02-28
 */
@Data
public class I18nLocaleInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @Excel(name = "编号")
    private Long localeId;

    /** 国家地区 */
    @Excel(name = "国家地区")
    private String localeName;

    /** 简称 */
    @Excel(name = "简称")
    private String locale;

    /** 状态（0正常 1隐藏） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=隐藏")
    private String localeStatus;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新人 */
    @Excel(name = "更新人")
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;


     /**
     * 对象转封装类
     *
     * @param i18nLocaleInfo I18nLocaleInfo实体对象
     * @return I18nLocaleInfoVo
     */
    public static I18nLocaleInfoVo objToVo(I18nLocaleInfo i18nLocaleInfo) {
        if (i18nLocaleInfo == null) {
            return null;
        }
        I18nLocaleInfoVo i18nLocaleInfoVo = new I18nLocaleInfoVo();
        BeanUtils.copyProperties(i18nLocaleInfo, i18nLocaleInfoVo);
        return i18nLocaleInfoVo;
    }
}
