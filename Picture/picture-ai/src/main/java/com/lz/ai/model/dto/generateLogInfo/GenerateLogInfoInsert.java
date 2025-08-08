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
public class GenerateLogInfoInsert implements Serializable {
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
     * @param generateLogInfoInsert 插入对象
     * @return GenerateLogInfoInsert
     */
    public static GenerateLogInfo insertToObj(GenerateLogInfoInsert generateLogInfoInsert) {
        if (generateLogInfoInsert == null) {
            return null;
        }
        GenerateLogInfo generateLogInfo = new GenerateLogInfo();
        BeanUtils.copyProperties(generateLogInfoInsert, generateLogInfo);
        return generateLogInfo;
    }
}
