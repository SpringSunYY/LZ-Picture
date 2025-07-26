package com.lz.config.model.dto.noticeInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.common.core.page.PageDomain;
import com.lz.config.model.domain.NoticeInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 用户公告Query对象 c_notice_info
 *
 * @author YY
 * @date 2025-07-26
 */
@Data
public class NoticeInfoRequest extends PageDomain implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 语言 默认zh-CN
     */
    private String locale;

    /**
     * 通知平台
     */
    private String platform;

    /**
     * 公告类型
     */
    private String noticeType;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 对象转封装类
     *
     * @param noticeInfoQuery 查询对象
     * @return NoticeInfo
     */
    public static NoticeInfo requestToObj(NoticeInfoRequest noticeInfoQuery) {
        if (noticeInfoQuery == null) {
            return null;
        }
        NoticeInfo noticeInfo = new NoticeInfo();
        BeanUtils.copyProperties(noticeInfoQuery, noticeInfo);
        return noticeInfo;
    }
}
