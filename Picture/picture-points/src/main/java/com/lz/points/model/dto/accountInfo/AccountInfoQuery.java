package com.lz.points.model.dto.accountInfo;

import java.util.Map;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.points.model.domain.AccountInfo;
/**
 * 积分账户Query对象 po_account_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class AccountInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 账户编号 */
    private String accountId;

    /** 用户编号 */
    private String userId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 删除（0否 1是） */
    private String isDelete;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param accountInfoQuery 查询对象
     * @return AccountInfo
     */
    public static AccountInfo queryToObj(AccountInfoQuery accountInfoQuery) {
        if (accountInfoQuery == null) {
            return null;
        }
        AccountInfo accountInfo = new AccountInfo();
        BeanUtils.copyProperties(accountInfoQuery, accountInfo);
        return accountInfo;
    }
}
