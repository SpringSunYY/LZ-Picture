package com.lz.ai.model.dto.officialUsageLogInfo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.ai.model.domain.OfficialUsageLogInfo;
/**
 * 官方AI操作日志Vo对象 ai_official_usage_log_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class OfficialUsageLogInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 记录编号 */
    private String logId;

    /** 管理员编号 */
    private Long userId;

    /** 模型编号 */
    private String modelId;

    /** 操作类型（如：data_analysis） */
    private String operationType;

    /** 输入参数 */
    private String inputParams;

    /** 模型返回结果（JSON/Text格式） */
    private String outputResult;

    /** 请求时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestTime;

    /** 请求时长（毫秒） */
    private Long requestDuration;

    /** 消耗Tokens数量 */
    private Long tokensUsed;

    /** 状态（0成功 1失败） */
    private String logStatus;

    /** 模型返回状态码 */
    private String aiStatusCode;

    /** 失败原因 */
    private String failReason;

    /** 备注 */
    private String remark;

    /** 删除（0正常 1删除） */
    private String isDelete;

    /**
     * 对象转封装类
     *
     * @param officialUsageLogInfoEdit 编辑对象
     * @return OfficialUsageLogInfo
     */
    public static OfficialUsageLogInfo editToObj(OfficialUsageLogInfoEdit officialUsageLogInfoEdit) {
        if (officialUsageLogInfoEdit == null) {
            return null;
        }
        OfficialUsageLogInfo officialUsageLogInfo = new OfficialUsageLogInfo();
        BeanUtils.copyProperties(officialUsageLogInfoEdit, officialUsageLogInfo);
        return officialUsageLogInfo;
    }
}
