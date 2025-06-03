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
public class SearchLogInfoInsert implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 搜索记录编号
     */
    private String searchId;

    /**
     * 对象转封装类
     *
     * @param searchLogInfoInsert 插入对象
     * @return SearchLogInfoInsert
     */
    public static SearchLogInfo insertToObj(SearchLogInfoInsert searchLogInfoInsert) {
        if (searchLogInfoInsert == null) {
            return null;
        }
        SearchLogInfo searchLogInfo = new SearchLogInfo();
        BeanUtils.copyProperties(searchLogInfoInsert, searchLogInfo);
        return searchLogInfo;
    }
}
