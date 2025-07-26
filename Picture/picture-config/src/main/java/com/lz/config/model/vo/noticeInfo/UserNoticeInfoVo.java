package com.lz.config.model.vo.noticeInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.config.model.domain.NoticeInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户公告Vo对象 c_notice_info
 *
 * @author YY
 * @date 2025-07-26
 */
@Data
public class UserNoticeInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 公告编号
     */
    private String noticeId;

    /**
     * 公告类型
     */
    private String noticeType;


    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /**
     * 对象转封装类
     *
     * @param noticeInfo NoticeInfo实体对象
     * @return NoticeInfoVo
     */
    public static UserNoticeInfoVo objToVo(NoticeInfo noticeInfo) {
        if (noticeInfo == null) {
            return null;
        }
        UserNoticeInfoVo noticeInfoVo = new UserNoticeInfoVo();
        BeanUtils.copyProperties(noticeInfo, noticeInfoVo);
        return noticeInfoVo;
    }
}
