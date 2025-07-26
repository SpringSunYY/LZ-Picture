package com.lz.config.model.dto.noticeInfo;

import com.lz.config.model.domain.NoticeInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 用户公告Vo对象 c_notice_info
 *
 * @author YY
 * @date 2025-07-26
 */
@Data
public class NoticeInfoEdit implements Serializable {
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
     * 公告内容
     */
    private String content;

    /**
     * 排序
     */
    private Long orderNum;

    /**
     * 公告状态
     */
    private String noticeStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param noticeInfoEdit 编辑对象
     * @return NoticeInfo
     */
    public static NoticeInfo editToObj(NoticeInfoEdit noticeInfoEdit) {
        if (noticeInfoEdit == null) {
            return null;
        }
        NoticeInfo noticeInfo = new NoticeInfo();
        BeanUtils.copyProperties(noticeInfoEdit, noticeInfo);
        return noticeInfo;
    }
}
