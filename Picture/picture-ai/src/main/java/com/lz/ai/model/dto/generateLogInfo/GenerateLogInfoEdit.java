package com.lz.ai.model.dto.generateLogInfo;

import com.lz.ai.model.domain.GenerateLogInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户生成记录Vo对象 ai_generate_log_info
 *
 * @author YY
 * @date 2025-08-08
 */
@Data
public class GenerateLogInfoEdit implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    private String logId;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 模型KEY
     */
    private String modelKey;

    /**
     * 模型类型
     */
    private String modelType;

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
