package com.lz.user.model.dto.statistics;

import com.lz.common.core.domain.statistics.ro.StatisticsRo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户信息统计Ro
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-12  18:50
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInformTypeStatisticsRo extends StatisticsRo {
    /**
     * 模板KEY
     */
    private String templateKey;

    /**
     * 模版类型（1短信 2邮件 3站内通知 4APP推送 5微信模板）
     */
    private String templateType;

    /**
     * 语言（默认zh-CN）
     */
    private String locale;

    /**
     * 发送时间
     */
    private String sendTime;
}
