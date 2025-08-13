package com.lz.ai.model.vo.generateLogInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.ai.model.domain.GenerateLogInfo;
import com.lz.common.annotation.Excel;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户生成记录Vo对象 ai_generate_log_info
 *
 * @author YY
 * @date 2025-08-08
 */
@Data
public class UserGenerateLogInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    private String logId;
    /**
     * 模型KEY
     */
    private String modelName;
    /**
     * 模型类型
     */
    private String modelType;
    /**
     * 提示词
     */
    private String prompt;
    /**
     * 负向提示词
     */
    private String negativePrompt;
    /**
     * 随机种子
     */
    private Float seed;
    /**
     * 文件地址
     */
    private String fileUrls;
    /**
     * 宽度
     */
    private Integer width;
    /**
     * 高度
     */
    private Integer height;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

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
    public static UserGenerateLogInfoVo objToVo(GenerateLogInfo generateLogInfo) {
        if (generateLogInfo == null) {
            return null;
        }
        UserGenerateLogInfoVo generateLogInfoVo = new UserGenerateLogInfoVo();
        BeanUtils.copyProperties(generateLogInfo, generateLogInfoVo);
        return generateLogInfoVo;
    }
}
