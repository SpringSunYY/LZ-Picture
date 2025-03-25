package com.lz.points.model.dto.accountInfo;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.points.model.domain.AccountInfo;
/**
 * 积分账户Vo对象 po_account_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class AccountInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 账户编号 */
    private String accountId;

    /** 用户编号 */
    private String userId;

    /** 支付密码 */
    private String password;

    /** 加密方式 */
    private String salt;

    /** 赚取总积分 */
    private Long pointsEarned;

    /** 使用总积分 */
    private Long pointsUsed;

    /** 充值总金额（元） */
    private BigDecimal rechargeAmount;

    /** 账户状态（0正常 1异常 2禁用） */
    private String accountStatus;

    /** 积分余额 */
    private Long pointsBalance;

    /** 备注 */
    private String remark;

    /** 删除（0否 1是） */
    private String isDelete;

    /**
     * 对象转封装类
     *
     * @param accountInfoEdit 编辑对象
     * @return AccountInfo
     */
    public static AccountInfo editToObj(AccountInfoEdit accountInfoEdit) {
        if (accountInfoEdit == null) {
            return null;
        }
        AccountInfo accountInfo = new AccountInfo();
        BeanUtils.copyProperties(accountInfoEdit, accountInfo);
        return accountInfo;
    }
}
