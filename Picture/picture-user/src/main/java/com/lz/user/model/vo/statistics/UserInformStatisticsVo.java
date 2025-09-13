package com.lz.user.model.vo.statistics;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.user.model.domain.InformInfo;
import com.lz.user.model.vo.informInfo.InformInfoVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户统计VO
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-13  16:33
 * @Version: 1.0
 */
@Data
public class UserInformStatisticsVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    /** 通知标题 */
    private String informTitle;

    /** 用户编号 */
    private String userId;
    private String userName;

    /** 发送状态（0=待发送 1=已发送 2=发送失败 3=已撤回） */
    private String status;

    /** 是否已读（0=未读 1=已读） */
    private String isRead;

    /** 发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    /**
     * 对象转封装类
     *
     * @param informInfo InformInfo实体对象
     * @return InformInfoVo
     */
    public static UserInformStatisticsVo objToVo(InformInfo informInfo) {
        if (informInfo == null) {
            return null;
        }
        UserInformStatisticsVo informInfoVo = new UserInformStatisticsVo();
        BeanUtils.copyProperties(informInfo, informInfoVo);
        return informInfoVo;
    }
}
