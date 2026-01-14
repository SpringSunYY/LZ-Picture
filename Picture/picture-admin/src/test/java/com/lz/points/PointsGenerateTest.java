package com.lz.points;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.utils.RandomUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.points.model.domain.*;
import com.lz.points.model.enums.*;
import com.lz.points.service.*;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.service.IUserInfoService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 积分模块测试类
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-26  14:29
 * @Version: 1.0
 */
@SpringBootTest
public class PointsGenerateTest {
    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private IAccountInfoService accountInfoService;

    @Resource
    private IPaymentOrderInfoService paymentOrderInfoService;

    @Resource
    private IPointsRechargePackageInfoService pointsRechargePackageInfoService;

    @Resource
    private IPointsRechargeInfoService pointsRechargeInfoService;

    @Resource
    private IPointsUsageLogInfoService pointsUsageLogInfoService;

    @Resource
    private IPoStatisticsInfoService poStatisticsInfoService;

    @Test
    public void testGenerateAccountInfo() {
        //生成账号，注意外键
        List<UserInfo> userInfoList = userInfoService.list(new LambdaQueryWrapper<UserInfo>().orderByDesc(UserInfo::getCreateTime).last("limit 10000"));
        ArrayList<AccountInfo> accountInfos = new ArrayList<>();
        for (UserInfo userInfo : userInfoList) {
            AccountInfo accountInfo = new AccountInfo();
            accountInfo.setAccountId(IdUtils.snowflakeId().toString());
            accountInfo.setUserId(userInfo.getUserId());
            accountInfo.setPassword(userInfo.getPassword());
            accountInfo.setSalt(userInfo.getSalt());
            accountInfo.setPointsEarned(0L);
            accountInfo.setPointsUsed(0L);
            accountInfo.setRechargeAmount(BigDecimal.valueOf(0));
            accountInfo.setAccountStatus(PoAccountStatusEnum.ACCOUNT_STATUS_0.getValue());
            accountInfo.setPointsBalance(0L);
            accountInfo.setCreateTime(userInfo.getCreateTime());
            accountInfo.setUpdateTime(userInfo.getUpdateTime());
            accountInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
            accountInfos.add(accountInfo);
        }
        accountInfoService.saveBatch(accountInfos);
    }

    @Test
    public void testUserRecharge() {
        //查询当前拥有账户
        List<AccountInfo> accountInfoList = accountInfoService.list(new LambdaQueryWrapper<AccountInfo>()
                .notIn(AccountInfo::getAccountId, List.of("1951241958457008130", "1951244312405270530", "1957116615768879106"))
                .last("limit 10000"));
        //查询充值套餐
        List<PointsRechargePackageInfo> pointsRechargePackageInfoList = pointsRechargePackageInfoService.list();
        int count = 0;
        ArrayList<PaymentOrderInfo> paymentOrderInfos = new ArrayList<>();
        ArrayList<PointsRechargeInfo> pointsRechargeInfos = new ArrayList<>();
        ArrayList<PointsUsageLogInfo> pointsUsageLogInfos = new ArrayList<>();
        for (AccountInfo accountInfo : accountInfoList) {
            for (PointsRechargePackageInfo packageInfo : pointsRechargePackageInfoList) {
                count++;
                PaymentOrderInfo orderInfo = new PaymentOrderInfo();
                String orderId = IdUtils.snowflakeId().toString();
                orderInfo.setOrderId(orderId);
                orderInfo.setUserId(accountInfo.getUserId());
                orderInfo.setOrderType(PoOrderTypeEnum.ORDER_TYPE_0.getValue());
                orderInfo.setOrderStatus(PoOrderStatusEnum.ORDER_STATUS_1.getValue());
                String paymentType = Long.parseLong(accountInfo.getAccountId()) % 2 == 0 ? PoPaymentTypeEnum.PAYMENT_TYPE_0.getValue() : PoPaymentTypeEnum.PAYMENT_TYPE_1.getValue();
                orderInfo.setPaymentType(paymentType);
                BigDecimal totalAmount = packageInfo.getPrice();
                orderInfo.setTotalAmount(totalAmount);
                orderInfo.setBuyerPayAmount(totalAmount);
                orderInfo.setReceiptAmount(totalAmount);
                orderInfo.setDiscountAmount(BigDecimal.valueOf(0));
                orderInfo.setThirdParty(paymentType.equals(PoPaymentTypeEnum.PAYMENT_TYPE_0.getValue()) ? "alipay_web" : "wx_web");
                orderInfo.setThirdUserId(accountInfo.getUserId());
                orderInfo.setThirdPartyOrder(orderId);
                Date paymentTime = RandomUtils.generateDate(2024, 2025);
                orderInfo.setPaymentTime(paymentTime);
                orderInfo.setPaymentStatus("TRADE_SUCCESS");
                orderInfo.setPaymentCode("10000");
                orderInfo.setPaymentMsg("Success");
                orderInfo.setPaymentExtend(null);
                orderInfo.setCreateTime(paymentTime);
                orderInfo.setUpdateTime(paymentTime);
                orderInfo.setDeviceId(null);
                orderInfo.setBrowser("Chrome");
                orderInfo.setOs("Windows 10");
                orderInfo.setPlatform("Windows");
                orderInfo.setIpAddr(RandomUtils.generateRandomIpAddr());
                orderInfo.setIpAddress(RandomUtils.generateRandomIPAddress());
                orderInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());

                paymentOrderInfos.add(orderInfo);

                PointsRechargeInfo rechargeInfo = new PointsRechargeInfo();
                rechargeInfo.setRechargeId(IdUtils.snowflakeId().toString());
                rechargeInfo.setPackageId(packageInfo.getPackageId());
                rechargeInfo.setPackageName(packageInfo.getPackageName());
                rechargeInfo.setUserId(accountInfo.getUserId());
                rechargeInfo.setOrderId(orderId);
                rechargeInfo.setTotalCount(packageInfo.getPoints() + packageInfo.getPointsBonus());
                rechargeInfo.setPointsCount(packageInfo.getPoints());
                rechargeInfo.setBonusCount(packageInfo.getPointsBonus());
                rechargeInfo.setPriceCount(totalAmount);
                rechargeInfo.setBuyerPayAmount(totalAmount);
                rechargeInfo.setRechargeCount(1L);
                rechargeInfo.setPaymentType(paymentType);
                rechargeInfo.setThirdParty(orderInfo.getThirdParty());
                rechargeInfo.setThirdPartyOrder(orderInfo.getThirdPartyOrder());
                rechargeInfo.setRechargeStatus(PoRechargeStatusEnum.RECHARGE_STATUS_1.getValue());
                rechargeInfo.setCreateTime(paymentTime);
                rechargeInfo.setArrivalTime(paymentTime);
                rechargeInfo.setUpdateTime(paymentTime);
                rechargeInfo.setDeviceId(null);
                rechargeInfo.setBrowser(orderInfo.getBrowser());
                rechargeInfo.setOs(orderInfo.getOs());
                rechargeInfo.setPlatform(orderInfo.getPlatform());
                rechargeInfo.setIpAddr(orderInfo.getIpAddr());
                rechargeInfo.setIpAddress(orderInfo.getIpAddress());
                rechargeInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
                pointsRechargeInfos.add(rechargeInfo);

                PointsUsageLogInfo pointsUsageLogInfo = new PointsUsageLogInfo();
                pointsUsageLogInfo.setLogId(IdUtils.snowflakeId().toString());
                pointsUsageLogInfo.setUserId(accountInfo.getUserId());
                pointsUsageLogInfo.setGiveUserId(null);
                pointsUsageLogInfo.setLogType(PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_0.getValue());
                pointsUsageLogInfo.setUsageType(null);
                pointsUsageLogInfo.setTargetId(rechargeInfo.getRechargeId());
                pointsUsageLogInfo.setPointsBefore(accountInfo.getPointsBalance());
                pointsUsageLogInfo.setPointsUsed(rechargeInfo.getTotalCount());
                //使用后积分
                accountInfo.setPointsBalance(accountInfo.getPointsBalance() + rechargeInfo.getTotalCount());

                pointsUsageLogInfo.setPointsAfter(accountInfo.getPointsBalance());
                pointsUsageLogInfo.setDeviceId(null);
                pointsUsageLogInfo.setBrowser(rechargeInfo.getBrowser());
                pointsUsageLogInfo.setOs(rechargeInfo.getOs());
                pointsUsageLogInfo.setPlatform(rechargeInfo.getPlatform());
                pointsUsageLogInfo.setIpAddr(rechargeInfo.getIpAddr());
                pointsUsageLogInfo.setIpAddress(rechargeInfo.getIpAddress());
                pointsUsageLogInfo.setCreateTime(paymentTime);
                pointsUsageLogInfo.setUpdateTime(paymentTime);
                pointsUsageLogInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
                pointsUsageLogInfos.add(pointsUsageLogInfo);

                accountInfo.setRechargeAmount(accountInfo.getRechargeAmount().add(totalAmount));
            }
        }
        System.out.println("count = " + count);
        paymentOrderInfoService.saveBatch(paymentOrderInfos);
        pointsRechargeInfoService.saveBatch(pointsRechargeInfos);
        pointsUsageLogInfoService.saveBatch(pointsUsageLogInfos);
        accountInfoService.updateBatchById(accountInfoList);
    }

    @Test
    public void testPointsUsage() {
        //查询当前拥有账户
        List<AccountInfo> accountInfoList = accountInfoService.list(new LambdaQueryWrapper<AccountInfo>()
                .notIn(AccountInfo::getAccountId, List.of("1951241958457008130", "1951244312405270530", "1957116615768879106"))
                .last("limit 10000"));
        List<PointsUsageLogInfo> pointsUsageLogInfos = new ArrayList<>();
        Long totalCount = 100L;
        for (AccountInfo accountInfo : accountInfoList) {
            String accountId = accountInfo.getAccountId();
            long l = Long.parseLong(accountId) % 3;
            for (int i = 0; i < 10; i++) {
                PointsUsageLogInfo pointsUsageLogInfo = new PointsUsageLogInfo();
                pointsUsageLogInfo.setLogId(IdUtils.snowflakeId().toString());
                pointsUsageLogInfo.setUserId(accountInfo.getUserId());
                pointsUsageLogInfo.setGiveUserId(null);
                pointsUsageLogInfo.setLogType(PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_1.getValue());
                pointsUsageLogInfo.setUsageType(String.valueOf(l));
                pointsUsageLogInfo.setTargetId(null);
                pointsUsageLogInfo.setPointsBefore(accountInfo.getPointsBalance());
                pointsUsageLogInfo.setPointsUsed(totalCount);
                //使用后积分
                accountInfo.setPointsBalance(accountInfo.getPointsBalance() + totalCount);
                pointsUsageLogInfo.setPointsAfter(accountInfo.getPointsBalance());
                pointsUsageLogInfo.setDeviceId(null);
                pointsUsageLogInfo.setBrowser("Chrome");
                pointsUsageLogInfo.setOs("Windows 11");
                pointsUsageLogInfo.setPlatform("Windows");
                pointsUsageLogInfo.setIpAddr(RandomUtils.generateRandomIpAddr());
                pointsUsageLogInfo.setIpAddress(RandomUtils.generateRandomIPAddress());
                Date createTime = RandomUtils.generateDate(2024, 2025);
                pointsUsageLogInfo.setCreateTime(createTime);
                pointsUsageLogInfo.setUpdateTime(createTime);
                pointsUsageLogInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
                pointsUsageLogInfos.add(pointsUsageLogInfo);
            }
        }
        pointsUsageLogInfoService.saveBatch(pointsUsageLogInfos);
        accountInfoService.updateBatchById(accountInfoList);
    }

    @Test
    public void testDeleteStatistics() {
        poStatisticsInfoService.remove(new LambdaQueryWrapper<>());
    }

    @Test
    public void testDeletePointsGenerate() {
        List<String> userIds = new ArrayList<>();
        userIds.add("1");
        userIds.add("2");
        userIds.add("009");
        //删除积分使用
        pointsUsageLogInfoService.remove(new LambdaQueryWrapper<PointsUsageLogInfo>().notIn(PointsUsageLogInfo::getUserId, userIds));
        //删除积分充值
        pointsRechargeInfoService.remove(new LambdaQueryWrapper<PointsRechargeInfo>().notIn(PointsRechargeInfo::getUserId, userIds));
        //删除账户信息
        accountInfoService.remove(new LambdaQueryWrapper<AccountInfo>().notIn(AccountInfo::getUserId, userIds));
    }
}
