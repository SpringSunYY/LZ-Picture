package com.lz.points.model.vo.accountInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.points.model.domain.AccountInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 积分账户Vo对象 po_account_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class UserAccountInfoVo implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;



    /** 赚取总积分 */
    @Excel(name = "赚取总积分")
    private Long pointsEarned;

    /** 使用总积分 */
    @Excel(name = "使用总积分")
    private Long pointsUsed;

    /** 充值总金额（元） */
    @Excel(name = "充值总金额", readConverterExp = "元=")
    private BigDecimal rechargeAmount;

    /** 账户状态（0正常 1异常 2禁用） */
    @Excel(name = "账户状态", readConverterExp = "0=正常,1=异常,2=禁用")
    private String accountStatus;

    /** 积分余额 */
    @Excel(name = "积分余额")
    private Long pointsBalance;

     /**
     * 对象转封装类
     *
     * @param accountInfo AccountInfo实体对象
     * @return AccountInfoVo
     */
    public static UserAccountInfoVo objToVo(AccountInfo accountInfo) {
        if (accountInfo == null) {
            return null;
        }
        UserAccountInfoVo accountInfoVo = new UserAccountInfoVo();
        BeanUtils.copyProperties(accountInfo, accountInfoVo);
        return accountInfoVo;
    }
}
