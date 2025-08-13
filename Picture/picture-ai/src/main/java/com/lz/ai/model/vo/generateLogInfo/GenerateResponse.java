package com.lz.ai.model.vo.generateLogInfo;

import com.lz.ai.model.domain.GenerateLogInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 用户生成记录Vo对象 ai_generate_log_info
 * ai生成返回对象
 *
 * @author YY
 * @date 2025-08-08
 */
@Data
public class GenerateResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    private String logId;

    /**
     * 任务编号
     */
    private String taskId;

    /**
     * 状态
     */
    private String logStatus;


    /**
     * 对象转封装类
     *
     * @param generateLogInfo GenerateLogInfo实体对象
     * @return GenerateLogInfoVo
     */
    public static GenerateResponse objToResponse(GenerateLogInfo generateLogInfo) {
        if (generateLogInfo == null) {
            return null;
        }
        GenerateResponse generateLogInfoVo = new GenerateResponse();
        BeanUtils.copyProperties(generateLogInfo, generateLogInfoVo);
        return generateLogInfoVo;
    }

    /**
     * 封装类转对象
     */
    public static List<GenerateResponse> objToResponse(List<GenerateLogInfo> generateLogInfoList) {
        return generateLogInfoList.stream().map(GenerateResponse::objToResponse).toList();
    }
}
