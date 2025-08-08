package com.lz.ai.model.dto.generateLogInfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.ai.model.domain.GenerateLogInfo;
/**
 * 用户生成记录Vo对象 ai_generate_log_info
 *
 * @author YY
 * @date 2025-08-08
 */
@Data
public class GenerateLogInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 记录编号 */
    private String logId;

    /** 任务编号 */
    private String taskId;

    /** 状态 */
    private String logStatus;

    /**
     * 对象转封装类
     *
     * @param generateLogInfoEdit 编辑对象
     * @return GenerateLogInfo
     */
    public static GenerateLogInfo editToObj(GenerateLogInfoEdit generateLogInfoEdit) {
        if (generateLogInfoEdit == null) {
            return null;
        }
        GenerateLogInfo generateLogInfo = new GenerateLogInfo();
        BeanUtils.copyProperties(generateLogInfoEdit, generateLogInfo);
        return generateLogInfo;
    }
}
