package com.lz.points.model.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.lz.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 积分账户对象 po_account_info
 *
 * @author YY
 * @date 2025-03-25
 */
@TableName("po_account_info")
@Data
public class AccountInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 账户编号
     */
    @Excel(name = "账户编号")
    @TableId(value = "account_id", type = IdType.ASSIGN_ID)
    private String accountId;

    /**
     * 用户编号
     */
    @Excel(name = "用户编号")
    private String userId;

    /**
     * 支付密码
     */
    @Excel(name = "支付密码")
    private String password;

    /**
     * 加密方式
     */
    @Excel(name = "加密方式")
    private String salt;

    /**
     * 赚取总积分
     */
    @Excel(name = "赚取总积分")
    private Long pointsEarned;

    /**
     * 使用总积分
     */
    @Excel(name = "使用总积分")
    private Long pointsUsed;

    /**
     * 充值总金额（元）
     */
    @Excel(name = "充值总金额", readConverterExp = "元=")
    private BigDecimal rechargeAmount;

    /**
     * 账户状态（0正常 1异常 2禁用）
     */
    @Excel(name = "账户状态", readConverterExp = "0=正常,1=异常,2=禁用")
    private String accountStatus;

    /**
     * 积分余额
     */
    @Excel(name = "积分余额")
    private Long pointsBalance;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 删除（0否 1是）
     */
    @Excel(name = "删除", readConverterExp = "0=否,1=是")
    private String isDelete;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
