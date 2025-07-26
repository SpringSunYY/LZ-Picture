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
public class NoticeInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 公告编号
     */
    @Excel(name = "公告编号")
    private String noticeId;

    /**
     * 语言 默认zh-CN
     */
    @Excel(name = "语言")
    private String locale;

    /**
     * 通知平台
     */
    @Excel(name = "通知平台")
    private String platform;

    /**
     * 公告类型
     */
    @Excel(name = "公告类型")
    private String noticeType;

    /**
     * 是否展示
     */
    @Excel(name = "是否展示")
    private String isExhibit;

    /**
     * 公告标题
     */
    @Excel(name = "公告标题")
    private String noticeTitle;

    /**
     * 公告内容
     */
    @Excel(name = "公告内容")
    private String content;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Long orderNum;

    /**
     * 公告状态
     */
    @Excel(name = "公告状态")
    private String noticeStatus;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    private String userName;
    private Long userId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;


    /**
     * 对象转封装类
     *
     * @param noticeInfo NoticeInfo实体对象
     * @return NoticeInfoVo
     */
    public static NoticeInfoVo objToVo(NoticeInfo noticeInfo) {
        if (noticeInfo == null) {
            return null;
        }
        NoticeInfoVo noticeInfoVo = new NoticeInfoVo();
        BeanUtils.copyProperties(noticeInfo, noticeInfoVo);
        return noticeInfoVo;
    }
}
