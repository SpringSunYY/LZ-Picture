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
public class GenerateLogInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @Excel(name = "记录编号")
    private String logId;

    /**
     * 用户编号
     */
    @Excel(name = "用户编号")
    private String userId;

    /**
     * 模型KEY
     */
    @Excel(name = "模型KEY")
    private String modelKey;

    /**
     * 模型类型
     */
    @Excel(name = "模型类型")
    private String modelType;

    /**
     * 输入文件
     */
    @Excel(name = "输入文件")
    private String inputFile;

    /**
     * 提示词
     */
    @Excel(name = "提示词")
    private String prompt;

    /**
     * 负向提示词
     */
    @Excel(name = "负向提示词")
    private String negativePrompt;

    /**
     * 随机种子
     */
    @Excel(name = "随机种子")
    private Float seed;

    /**
     * 数量
     */
    @Excel(name = "数量")
    private Integer numbers;

    /**
     * 输入参数
     */
    @Excel(name = "输入参数")
    private String inputParams;

    /**
     * 任务编号
     */
    @Excel(name = "任务编号")
    private String taskId;

    /**
     * 返回结果
     */
    @Excel(name = "返回结果")
    private String outputResult;

    /**
     * 文件地址
     */
    @Excel(name = "文件地址")
    private String fileUrls;

    /**
     * 宽度
     */
    @Excel(name = "宽度")
    private Integer width;

    /**
     * 高度
     */
    @Excel(name = "高度")
    private Integer height;

    /**
     * 请求时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "请求时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date requestTime;

    /**
     * 请求时长
     */
    @Excel(name = "请求时长")
    private Long requestDuration;

    /**
     * 价格
     */
    @Excel(name = "价格")
    private BigDecimal priceUsed;

    /**
     * 消耗的积分
     */
    @Excel(name = "消耗的积分")
    private Long pointsUsed;

    /**
     * 参考对象
     */
    @Excel(name = "参考对象")
    private String targetId;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String logStatus;

    /**
     * 模型返回码
     */
    @Excel(name = "模型返回码")
    private String aiStatusCode;

    /**
     * 失败原因
     */
    @Excel(name = "失败原因")
    private String failReason;

    /**
     * 是否统计
     */
    @Excel(name = "是否统计")
    private String hasStatistics;

    /**
     * 用户IP地址
     */
    @Excel(name = "用户IP地址")
    private String ipAddr;

    /**
     * 用户设备唯一标识
     */
    @Excel(name = "用户设备唯一标识")
    private String deviceId;

    /**
     * 浏览器类型
     */
    @Excel(name = "浏览器类型")
    private String browser;

    /**
     * 操作系统
     */
    @Excel(name = "操作系统")
    private String os;

    /**
     * 平台
     */
    @Excel(name = "平台")
    private String platform;

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
     * 删除
     */
    @Excel(name = "删除")
    private String isDelete;


    /**
     * 对象转封装类
     *
     * @param generateLogInfo GenerateLogInfo实体对象
     * @return GenerateLogInfoVo
     */
    public static GenerateLogInfoVo objToVo(GenerateLogInfo generateLogInfo) {
        if (generateLogInfo == null) {
            return null;
        }
        GenerateLogInfoVo generateLogInfoVo = new GenerateLogInfoVo();
        BeanUtils.copyProperties(generateLogInfo, generateLogInfoVo);
        return generateLogInfoVo;
    }
}
