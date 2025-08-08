package com.lz.ai.model.dto.generateLogInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.ai.model.domain.GenerateLogInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 用户生成记录Query对象 ai_generate_log_info
 *
 * @author YY
 * @date 2025-08-08
 */
@Data
public class GenerateLogInfoQuery implements Serializable {
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
    private Long seed;

    /**
     * 数量
     */
    private Long numbers;

    /**
     * 任务编号
     */
    private String taskId;

    /**
     * 宽度
     */
    private Long width;

    /**
     * 高度
     */
    private Long height;

    /**
     * 请求时长
     */
    private Long requestDuration;

    /**
     * 参考对象
     */
    private String targetId;

    /**
     * 状态
     */
    private String logStatus;

    /**
     * 模型返回码
     */
    private String aiStatusCode;

    /**
     * 用户IP地址
     */
    private String ipAddr;

    /**
     * 用户设备唯一标识
     */
    private String deviceId;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 平台
     */
    private String platform;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /**
     * 删除
     */
    private String isDelete;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param generateLogInfoQuery 查询对象
     * @return GenerateLogInfo
     */
    public static GenerateLogInfo queryToObj(GenerateLogInfoQuery generateLogInfoQuery) {
        if (generateLogInfoQuery == null) {
            return null;
        }
        GenerateLogInfo generateLogInfo = new GenerateLogInfo();
        BeanUtils.copyProperties(generateLogInfoQuery, generateLogInfo);
        return generateLogInfo;
    }
}
