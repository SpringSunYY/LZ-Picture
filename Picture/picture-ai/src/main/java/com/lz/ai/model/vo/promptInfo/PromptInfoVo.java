package com.lz.ai.model.vo.promptInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.ai.model.domain.PromptInfo;
/**
 * 提示词信息Vo对象 ai_prompt_info
 *
 * @author YY
 * @date 2025-08-08
 */
@Data
public class PromptInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @Excel(name = "编号")
    private String infoId;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 提示内容 */
    @Excel(name = "提示内容")
    private String content;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新人 */
    @Excel(name = "更新人")
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;


     /**
     * 对象转封装类
     *
     * @param promptInfo PromptInfo实体对象
     * @return PromptInfoVo
     */
    public static PromptInfoVo objToVo(PromptInfo promptInfo) {
        if (promptInfo == null) {
            return null;
        }
        PromptInfoVo promptInfoVo = new PromptInfoVo();
        BeanUtils.copyProperties(promptInfo, promptInfoVo);
        return promptInfoVo;
    }
}
