package com.lz.ai.model.dto.promptInfo;

import com.lz.common.core.page.PageDomain;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 提示词信息Query对象 ai_prompt_info
 * 用户查询
 *
 * @author YY
 * @date 2025-08-14
 */
@Data
public class PromptInfoRequest extends PageDomain implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
