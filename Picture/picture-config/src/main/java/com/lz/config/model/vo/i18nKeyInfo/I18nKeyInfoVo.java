package com.lz.config.model.vo.i18nKeyInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.config.model.domain.I18nKeyInfo;
/**
 * 国际化键名Vo对象 c_i18n_key_info
 *
 * @author ruoyi
 * @date 2025-02-28
 */
@Data
public class I18nKeyInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @Excel(name = "编号")
    private Long keyId;

    /** 键 */
    @Excel(name = "键")
    private String keyName;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Long orderNum;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;

    /** 更新人 */
    @Excel(name = "更新人")
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateTime;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;


     /**
     * 对象转封装类
     *
     * @param i18nKeyInfo I18nKeyInfo实体对象
     * @return I18nKeyInfoVo
     */
    public static I18nKeyInfoVo objToVo(I18nKeyInfo i18nKeyInfo) {
        if (i18nKeyInfo == null) {
            return null;
        }
        I18nKeyInfoVo i18nKeyInfoVo = new I18nKeyInfoVo();
        BeanUtils.copyProperties(i18nKeyInfo, i18nKeyInfoVo);
        return i18nKeyInfoVo;
    }
}
