package com.lz.config.model.dto.noticeInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.config.model.domain.NoticeInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

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
public class NoticeInfoQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 公告编号
     */
    private String noticeId;

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
     * 是否展示
     */
    private String isExhibit;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 公告状态
     */
    private String noticeStatus;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param noticeInfoQuery 查询对象
     * @return NoticeInfo
     */
    public static NoticeInfo queryToObj(NoticeInfoQuery noticeInfoQuery) {
        if (noticeInfoQuery == null) {
            return null;
        }
        NoticeInfo noticeInfo = new NoticeInfo();
        BeanUtils.copyProperties(noticeInfoQuery, noticeInfo);
        return noticeInfo;
    }
}
