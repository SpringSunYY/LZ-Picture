package com.lz.ai.model.dto.promptInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.ai.model.domain.PromptInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 提示词信息Query对象 ai_prompt_info
 *
 * @author YY
 * @date 2025-08-14
 */
@Data
public class PromptInfoQuery implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private String infoId;

    /**
     * 名称
     */
    private String name;

    /**
     * 提示内容
     */
    private String content;

    /**
     * 状态
     */
    private String promptStatus;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param promptInfoQuery 查询对象
     * @return PromptInfo
     */
    public static PromptInfo queryToObj(PromptInfoQuery promptInfoQuery) {
        if (promptInfoQuery == null) {
            return null;
        }
        PromptInfo promptInfo = new PromptInfo();
        BeanUtils.copyProperties(promptInfoQuery, promptInfo);
        return promptInfo;
    }
}
