package com.lz.ai.model.dto.promptInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.ai.model.domain.PromptInfo;
/**
 * 提示词信息Vo对象 ai_prompt_info
 *
 * @author YY
 * @date 2025-08-08
 */
@Data
public class PromptInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private String infoId;

    /** 名称 */
    private String name;

    /** 提示内容 */
    private String content;

    /** 排序 */
    private Long orderNum;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param promptInfoEdit 编辑对象
     * @return PromptInfo
     */
    public static PromptInfo editToObj(PromptInfoEdit promptInfoEdit) {
        if (promptInfoEdit == null) {
            return null;
        }
        PromptInfo promptInfo = new PromptInfo();
        BeanUtils.copyProperties(promptInfoEdit, promptInfo);
        return promptInfo;
    }
}
