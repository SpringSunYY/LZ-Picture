package com.lz.config.model.vo.i18nMessageInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.config.model.domain.I18nMessageInfo;
/**
 * 国际化信息Vo对象 c_i18n_message_info
 *
 * @author ruoyi
 * @date 2025-02-28
 */
@Data
public class I18nMessageInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Excel(name = "主键")
    private Long messageId;

    /** 键 */
    @Excel(name = "键")
    private String messageKey;

    /** 简称 */
    @Excel(name = "简称")
    private String locale;

    /** 消息 */
    @Excel(name = "消息")
    private String message;

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
     * @param i18nMessageInfo I18nMessageInfo实体对象
     * @return I18nMessageInfoVo
     */
    public static I18nMessageInfoVo objToVo(I18nMessageInfo i18nMessageInfo) {
        if (i18nMessageInfo == null) {
            return null;
        }
        I18nMessageInfoVo i18nMessageInfoVo = new I18nMessageInfoVo();
        BeanUtils.copyProperties(i18nMessageInfo, i18nMessageInfoVo);
        return i18nMessageInfoVo;
    }
}
