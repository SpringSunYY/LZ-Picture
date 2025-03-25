package com.lz.points.model.dto.withdrawalOrderInfo;

import java.util.Map;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.points.model.domain.WithdrawalOrderInfo;
/**
 * 用户提现记录Query对象 po_withdrawal_order_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class WithdrawalOrderInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 提现订单编号 */
    private String withdrawalId;

    /** 用户编号 */
    private String userId;

    /** 提现方式（0支付宝 1微信） */
    private String withdrawalMethod;

    /** 提现账户 */
    private String withdrawalAccount;

    /** 提现状态（0待处理 1完成 2失败 3超时） */
    private String withdrawalStatus;

    /** 提现平台订单号 */
    private String withdrawalPlatformOrder;

    /** 交易编号 */
    private String transactionId;

    /** 审核状态（0待审核 1同意 2拒绝） */
    private String reviewStatus;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reviewTime;

    /** 审核人编号 */
    private Long reviewUserId;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date accomplishTime;

    /** 设备唯一标识 */
    private String deviceId;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 平台 */
    private String platform;

    /** IP地址 */
    private String ipAddr;

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
     * @param withdrawalOrderInfoQuery 查询对象
     * @return WithdrawalOrderInfo
     */
    public static WithdrawalOrderInfo queryToObj(WithdrawalOrderInfoQuery withdrawalOrderInfoQuery) {
        if (withdrawalOrderInfoQuery == null) {
            return null;
        }
        WithdrawalOrderInfo withdrawalOrderInfo = new WithdrawalOrderInfo();
        BeanUtils.copyProperties(withdrawalOrderInfoQuery, withdrawalOrderInfo);
        return withdrawalOrderInfo;
    }
}
