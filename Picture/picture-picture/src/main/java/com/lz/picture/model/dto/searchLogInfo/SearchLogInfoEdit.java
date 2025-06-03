package com.lz.picture.model.dto.searchLogInfo;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.SearchLogInfo;

/**
 * 用户搜索记录Vo对象 p_search_log_info
 *
 * @author YY
 * @date 2025-06-03
 */
@Data
public class SearchLogInfoEdit implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 搜索记录编号
     */
    private String searchId;

    /**
     * 对象转封装类
     *
     * @param searchLogInfoEdit 编辑对象
     * @return SearchLogInfo
     */
    public static SearchLogInfo editToObj(SearchLogInfoEdit searchLogInfoEdit) {
        if (searchLogInfoEdit == null) {
            return null;
        }
        SearchLogInfo searchLogInfo = new SearchLogInfo();
        BeanUtils.copyProperties(searchLogInfoEdit, searchLogInfo);
        return searchLogInfo;
    }
}
