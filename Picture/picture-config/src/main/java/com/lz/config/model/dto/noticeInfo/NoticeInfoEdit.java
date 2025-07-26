package com.lz.config.model.dto.noticeInfo;

import com.lz.config.model.domain.NoticeInfo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "语言不能为空")
    private String locale;

    /**
     * 通知平台
     */
    @NotEmpty(message = "通知平台不能为空")
    private String platform;

    /**
     * 公告类型
     */
    @NotEmpty(message = "公告类型不能为空")
    private String noticeType;

    /**
     * 是否展示
     */
    @NotEmpty(message = "是否展示不能为空")
    private String isExhibit;

    /**
     * 公告标题
     */
    @NotEmpty(message = "公告标题不能为空")
    private String noticeTitle;

    /**
     * 公告内容
     */
    @NotEmpty(message = "公告内容不能为空")
    private String content;

    /**
     * 排序
     */
    @Max(value = 10,message = "排序不能大于10")
    @Min(value = 0,message = "排序不能小于0")
    private Long orderNum;

    /**
     * 公告状态
     */
    @NotEmpty(message = "公告状态不能为空")
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
