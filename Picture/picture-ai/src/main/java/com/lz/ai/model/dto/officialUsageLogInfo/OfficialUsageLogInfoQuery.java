package com.lz.ai.model.dto.officialUsageLogInfo;

import java.util.Map;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.ai.model.domain.OfficialUsageLogInfo;
/**
 * 官方AI操作日志Query对象 ai_official_usage_log_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class OfficialUsageLogInfoQuery implements Serializable
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

    /** 状态（0成功 1失败） */
    private String logStatus;

    /** 模型返回状态码 */
    private String aiStatusCode;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 删除（0正常 1删除） */
    private String isDelete;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param officialUsageLogInfoQuery 查询对象
     * @return OfficialUsageLogInfo
     */
    public static OfficialUsageLogInfo queryToObj(OfficialUsageLogInfoQuery officialUsageLogInfoQuery) {
        if (officialUsageLogInfoQuery == null) {
            return null;
        }
        OfficialUsageLogInfo officialUsageLogInfo = new OfficialUsageLogInfo();
        BeanUtils.copyProperties(officialUsageLogInfoQuery, officialUsageLogInfo);
        return officialUsageLogInfo;
    }
}
