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
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.*;

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
    private TransactionTemplate transactionTemplate;

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
        //把充值套餐转换为map，key为list索引，value为充值套餐
        HashMap<Integer, PointsRechargePackageInfo> packageInfoHashMap = new HashMap<>();
        for (int i = 0; i < pointsRechargePackageInfoList.size(); i++) {
            PointsRechargePackageInfo pointsRechargePackageInfo = pointsRechargePackageInfoList.get(i);
            packageInfoHashMap.put(i, pointsRechargePackageInfo);
        }
        int count = 0;
        int index = 1;
        int max = pointsRechargePackageInfoList.size();
        Random random = new Random();

        ArrayList<PaymentOrderInfo> paymentOrderInfos = new ArrayList<>();
        ArrayList<PointsRechargeInfo> pointsRechargeInfos = new ArrayList<>();
        ArrayList<PointsUsageLogInfo> pointsUsageLogInfos = new ArrayList<>();
        for (AccountInfo accountInfo : accountInfoList) {
            for (int i = 0; i < 10; i++) {
                count++;
                int nextInt = random.nextInt(index);
                index++;
                if (index > max) {
                    nextInt = 0;
                }
                PointsRechargePackageInfo packageInfo = packageInfoHashMap.get(nextInt);

                PaymentOrderInfo orderInfo = new PaymentOrderInfo();
                String orderId = IdUtils.snowflakeId().toString();
                orderInfo.setOrderId(orderId);
                orderInfo.setUserId(accountInfo.getUserId());
                orderInfo.setOrderType(PoOrderTypeEnum.ORDER_TYPE_0.getValue());
                orderInfo.setOrderStatus(PoAccountStatusEnum.ACCOUNT_STATUS_0.getValue());
                String paymentType = nextInt % 2 == 0 ? PoPaymentTypeEnum.PAYMENT_TYPE_0.getValue() : PoPaymentTypeEnum.PAYMENT_TYPE_1.getValue();
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
        transactionTemplate.executeWithoutResult(status -> {
            paymentOrderInfoService.saveBatch(paymentOrderInfos);
            pointsRechargeInfoService.saveBatch(pointsRechargeInfos);
            pointsUsageLogInfoService.saveBatch(pointsUsageLogInfos);
            accountInfoService.updateBatchById(accountInfoList);
        });
    }
}
