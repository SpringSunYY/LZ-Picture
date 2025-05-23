package com.lz.picture.model.dto.userViewLogInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.common.core.page.PageDomain;
import com.lz.picture.model.domain.UserViewLogInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 用户浏览记录Query对象 用户自己查询
 *
 * @author YY
 * @date 2025-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MyUserViewLogInfoQuery extends PageDomain implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 目标类型
     */
    private String targetType;

    /**
     * 目标内容
     */
    private String targetContent;

    /**
     * 查看时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param userViewLogInfoQuery 查询对象
     * @return UserViewLogInfo
     */
    public static UserViewLogInfo queryToObj(MyUserViewLogInfoQuery userViewLogInfoQuery) {
        if (userViewLogInfoQuery == null) {
            return null;
        }
        UserViewLogInfo userViewLogInfo = new UserViewLogInfo();
        BeanUtils.copyProperties(userViewLogInfoQuery, userViewLogInfo);
        return userViewLogInfo;
    }
}
