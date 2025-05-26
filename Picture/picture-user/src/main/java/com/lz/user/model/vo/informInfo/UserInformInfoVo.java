package com.lz.user.model.vo.informInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.user.model.domain.InformInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户通知记录Vo对象 u_inform_info
 *
 * @author YY
 * @date 2025-05-27
 */
@Data
public class UserInformInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 通知记录编号
     */
    @Excel(name = "通知记录编号")
    private String recordId;

    /**
     * 通知标题
     */
    private String informTitle;

    /**
     * 模版类型（1短信 2邮件 3站内通知 4APP推送 5微信模板）
     */
    @Excel(name = "模版类型", readConverterExp = "1=短信,2=邮件,3=站内通知,4=APP推送,5=微信模板")
    private String templateType;


    /**
     * 实际发送内容
     */
    @Excel(name = "实际发送内容")
    private String content;

    /**
     * 通知类型
     */
    @Excel(name = "通知类型", readConverterExp = "1=公共,0=通知")
    private String informType;

    /**
     * 是否已读（0=未读 1=已读）
     */
    @Excel(name = "是否已读", readConverterExp = "0=未读,1=已读")
    private Integer isRead;


    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发送时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;


    /**
     * 对象转封装类
     *
     * @param informInfo InformInfo实体对象
     * @return InformInfoVo
     */
    public static UserInformInfoVo objToVo(InformInfo informInfo) {
        if (informInfo == null) {
            return null;
        }
        UserInformInfoVo informInfoVo = new UserInformInfoVo();
        BeanUtils.copyProperties(informInfo, informInfoVo);
        return informInfoVo;
    }

    /**
     * 封装类转对象
     *
     * @param informInfoList InformInfoVo封装类
     * @return InformInfo
     */
    public static List<UserInformInfoVo> objToVo(List<InformInfo> informInfoList) {
        return informInfoList.stream().map(UserInformInfoVo::objToVo).toList();
    }
}
