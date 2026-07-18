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
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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

    // ==================== 全局可配置参数 ====================

    /**
     * 不需要生成数据的用户列表
     */
    private static final List<String> EXCLUDED_USER_IDS = List.of("1", "2", "009");

    /**
     * 排除的账户
     */
    private static final List<String> EXCLUDED_ACCOUNT_IDS = List.of(
            "1951241958457008130",
            "1951244312405270530",
            "1957116615768879106"
    );

    /**
     * 充值记录目标总数
     */
    private static final long TARGET_RECHARGE_COUNT = 500_000L;

    /**
     * 积分使用记录目标总数
     */
    private static final long TARGET_USAGE_COUNT = 1000_000L;

    /**
     * 比指定时间晚的最小分钟数
     */
    private static final int MIN_MINUTES = 1;

    /**
     * 比指定时间按晚的最大分钟数
     */
    private static final int MAX_MINUTES = 720;

    /**
     * 批量数量
     */
    private static final int BATCH_SIZE = 10000;

    // ==================== 数据生成方法 ====================

    @Test
    public void testGenerate() {
        long startTime = System.currentTimeMillis();
        p("开始生成数据...");
        testGenerateAccountInfo();
        testUserRecharge();
        testPointsUsage();
        p("数据生成完成", startTime);
    }

    /**
     * 生成账号，账号创建时间与用户注册时间保持一致。
     */
    @Test
    public void testGenerateAccountInfo() {
        long startTime = System.currentTimeMillis();
        p("开始生成账户信息...");

        List<UserInfo> userInfoList = userInfoService.list(
                new LambdaQueryWrapper<UserInfo>()
                        .notIn(UserInfo::getUserId, EXCLUDED_USER_IDS)
                        .orderByDesc(UserInfo::getCreateTime));

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
        p("账户生成完成，共 " + accountInfos.size() + " 条", startTime);
    }

    /**
     * 用户充值——充值金额采用小额高频、大额低频的真实分布规律；
     * 充值时间严格晚于用户注册时间，且在同一账户内按时间顺序递增。
     * 充值积分 = 套餐积分 + 赠送积分。
     * <p>
     * 分布策略：以套餐价格为权重（weight = 1/price）进行加权随机抽取，
     * 价格越低的套餐权重越高，被选中的概率越大，契合真实充值习惯。
     */
    @Test
    public void testUserRecharge() {
        long startTime = System.currentTimeMillis();
        p("开始生成充值记录，目标数量：" + TARGET_RECHARGE_COUNT);

        // 查询当前拥有账户（排除指定账户和系统用户）
        List<AccountInfo> accountInfoList = accountInfoService.list(
                new LambdaQueryWrapper<AccountInfo>()
                        .notIn(AccountInfo::getUserId, EXCLUDED_USER_IDS)
                        .notIn(AccountInfo::getAccountId, EXCLUDED_ACCOUNT_IDS));
        int totalAccounts = accountInfoList.size();
        if (totalAccounts == 0) {
            p("没有可用账户");
            return;
        }

        // 加载用户真实 IP
        List<String> userIds = accountInfoList.stream().map(AccountInfo::getUserId).toList();
        List<UserInfo> userList = userInfoService.list(
                new LambdaQueryWrapper<UserInfo>().in(UserInfo::getUserId, userIds));
        Map<String, UserInfo> userMap = userList.stream().collect(java.util.stream.Collectors.toMap(UserInfo::getUserId, u -> u));
        Map<String, String> userIpAddrMap = new HashMap<>();
        Map<String, String> userIpAddressMap = new HashMap<>();
        for (AccountInfo acc : accountInfoList) {
            UserInfo u = userMap.get(acc.getUserId());
            userIpAddrMap.put(acc.getUserId(), u != null ? u.getLastLoginIp() : RandomUtils.generateRandomIpAddr());
            userIpAddressMap.put(acc.getUserId(), u != null ? u.getIpAddress() : RandomUtils.generateRandomIPAddress());
        }

        // 查询充值套餐列表
        List<PointsRechargePackageInfo> pointsRechargePackageInfoList = pointsRechargePackageInfoService.list().stream()
                .toList();

        // 计算权重：weight = 1 / (1 + 0.5*log(price))，价格低权重稍大但不会悬殊
        double[] packageWeights = new double[pointsRechargePackageInfoList.size()];
        double totalWeight = 0;
        for (int i = 0; i < pointsRechargePackageInfoList.size(); i++) {
            BigDecimal price = pointsRechargePackageInfoList.get(i).getPrice();
            double w = (price != null && price.compareTo(BigDecimal.ZERO) > 0)
                    ? 1.0 / (1 + 0.5 * Math.log(price.doubleValue())) : 0;
            packageWeights[i] = w;
            totalWeight += w;
        }

        // 计算每个账户应分配的充值数（前多后少）
        long[] accountAllocation = computeLinearAllocation(TARGET_RECHARGE_COUNT, totalAccounts);

        ArrayList<PaymentOrderInfo> paymentOrderInfos = new ArrayList<>();
        ArrayList<PointsRechargeInfo> pointsRechargeInfos = new ArrayList<>();
        ArrayList<PointsUsageLogInfo> pointsUsageLogInfos = new ArrayList<>();
        long inserted = 0;
        long lastPrint = 0;

        for (int ai = 0; ai < totalAccounts; ai++) {
            AccountInfo accountInfo = accountInfoList.get(ai);
            int rechargeCount = (int) accountAllocation[ai];
            Date lastRechargeTime = accountInfo.getCreateTime();

            for (int i = 0; i < rechargeCount; i++) {
                lastRechargeTime = RandomUtils.generateDateAfter(lastRechargeTime, MIN_MINUTES, MAX_MINUTES);

                // 按权重随机选套餐
                double rand = Math.random() * totalWeight;
                double cumulative = 0;
                PointsRechargePackageInfo packageInfo = null;
                for (int pi = 0; pi < pointsRechargePackageInfoList.size(); pi++) {
                    cumulative += packageWeights[pi];
                    if (rand <= cumulative) {
                        packageInfo = pointsRechargePackageInfoList.get(pi);
                        break;
                    }
                }

                BigDecimal totalAmount = packageInfo.getPrice();
                long totalPoints = packageInfo.getPoints() + packageInfo.getPointsBonus();

                PaymentOrderInfo orderInfo = new PaymentOrderInfo();
                String orderId = IdUtils.snowflakeId().toString();
                orderInfo.setOrderId(orderId);
                orderInfo.setUserId(accountInfo.getUserId());
                orderInfo.setOrderType(PoOrderTypeEnum.ORDER_TYPE_0.getValue());
                orderInfo.setOrderStatus(PoOrderStatusEnum.ORDER_STATUS_1.getValue());
                String paymentType = Long.parseLong(accountInfo.getAccountId()) % 2 == 0
                        ? PoPaymentTypeEnum.PAYMENT_TYPE_0.getValue()
                        : PoPaymentTypeEnum.PAYMENT_TYPE_1.getValue();
                orderInfo.setPaymentType(paymentType);
                orderInfo.setTotalAmount(totalAmount);
                orderInfo.setBuyerPayAmount(totalAmount);
                orderInfo.setReceiptAmount(totalAmount);
                orderInfo.setDiscountAmount(BigDecimal.valueOf(0));
                orderInfo.setThirdParty(paymentType.equals(PoPaymentTypeEnum.PAYMENT_TYPE_0.getValue())
                        ? "alipay_web" : "wx_web");
                orderInfo.setThirdUserId(accountInfo.getUserId());
                orderInfo.setThirdPartyOrder(orderId);
                orderInfo.setPaymentTime(lastRechargeTime);
                orderInfo.setPaymentStatus("TRADE_SUCCESS");
                orderInfo.setPaymentCode("10000");
                orderInfo.setPaymentMsg("Success");
                orderInfo.setPaymentExtend(null);
                orderInfo.setCreateTime(lastRechargeTime);
                orderInfo.setUpdateTime(lastRechargeTime);
                orderInfo.setDeviceId(null);
                orderInfo.setBrowser("Chrome");
                orderInfo.setOs("Windows 10");
                orderInfo.setPlatform("Windows");
                orderInfo.setIpAddr(userIpAddrMap.get(accountInfo.getUserId()));
                orderInfo.setIpAddress(userIpAddressMap.get(accountInfo.getUserId()));
                orderInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
                paymentOrderInfos.add(orderInfo);

                PointsRechargeInfo rechargeInfo = new PointsRechargeInfo();
                rechargeInfo.setRechargeId(IdUtils.snowflakeId().toString());
                rechargeInfo.setPackageId(packageInfo.getPackageId());
                rechargeInfo.setPackageName(packageInfo.getPackageName());
                rechargeInfo.setUserId(accountInfo.getUserId());
                rechargeInfo.setOrderId(orderId);
                rechargeInfo.setTotalCount(totalPoints);
                rechargeInfo.setPointsCount(packageInfo.getPoints());
                rechargeInfo.setBonusCount(packageInfo.getPointsBonus());
                rechargeInfo.setPriceCount(totalAmount);
                rechargeInfo.setBuyerPayAmount(totalAmount);
                rechargeInfo.setRechargeCount(1L);
                rechargeInfo.setPaymentType(paymentType);
                rechargeInfo.setThirdParty(orderInfo.getThirdParty());
                rechargeInfo.setThirdPartyOrder(orderInfo.getThirdPartyOrder());
                rechargeInfo.setRechargeStatus(PoRechargeStatusEnum.RECHARGE_STATUS_1.getValue());
                rechargeInfo.setCreateTime(lastRechargeTime);
                rechargeInfo.setArrivalTime(lastRechargeTime);
                rechargeInfo.setUpdateTime(lastRechargeTime);
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
                pointsUsageLogInfo.setPointsUsed(totalPoints);
                accountInfo.setPointsBalance(accountInfo.getPointsBalance() + totalPoints);
                pointsUsageLogInfo.setPointsAfter(accountInfo.getPointsBalance());
                pointsUsageLogInfo.setDeviceId(null);
                pointsUsageLogInfo.setBrowser(rechargeInfo.getBrowser());
                pointsUsageLogInfo.setOs(rechargeInfo.getOs());
                pointsUsageLogInfo.setPlatform(rechargeInfo.getPlatform());
                pointsUsageLogInfo.setIpAddr(rechargeInfo.getIpAddr());
                pointsUsageLogInfo.setIpAddress(rechargeInfo.getIpAddress());
                pointsUsageLogInfo.setCreateTime(lastRechargeTime);
                pointsUsageLogInfo.setUpdateTime(lastRechargeTime);
                pointsUsageLogInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
                pointsUsageLogInfos.add(pointsUsageLogInfo);

                accountInfo.setRechargeAmount(accountInfo.getRechargeAmount().add(totalAmount));
                inserted++;
                if (inserted - lastPrint >= 100000) {
                    p("充值记录生成进度：" + inserted + " / " + TARGET_RECHARGE_COUNT + "（" + String.format("%.1f", (double) inserted / TARGET_RECHARGE_COUNT * 100) + "%）", startTime);
                    lastPrint = inserted;
                }
            }
        }
        p("总订单= " + paymentOrderInfos.size() + "，开始写入", startTime);
        List<List<PaymentOrderInfo>> orderBatches = partition(paymentOrderInfos, BATCH_SIZE);
        List<List<PointsRechargeInfo>> rechargeBatches = partition(pointsRechargeInfos, BATCH_SIZE);
        List<List<PointsUsageLogInfo>> usageBatches = partition(pointsUsageLogInfos, BATCH_SIZE);

        int orderIndex = 0;
        int orderSavedSize = 0;
        for (List<PaymentOrderInfo> batch : orderBatches) {
            paymentOrderInfoService.saveBatch(batch);
            orderIndex++;
            orderSavedSize += batch.size();
            p("1 订单写入批次：" + orderIndex + " / " + orderBatches.size() + "，累计数量：" + orderSavedSize, startTime);
        }

        int rechargeIndex = 0;
        int rechargeSavedSize = 0;
        for (List<PointsRechargeInfo> batch : rechargeBatches) {
            pointsRechargeInfoService.saveBatch(batch);
            rechargeIndex++;
            rechargeSavedSize += batch.size();
            p("2 充值记录写入批次：" + rechargeIndex + " / " + rechargeBatches.size() + "，累计数量：" + rechargeSavedSize, startTime);
        }

        int usageIndex = 0;
        int usageSavedSize = 0;
        for (List<PointsUsageLogInfo> batch : usageBatches) {
            pointsUsageLogInfoService.saveBatch(batch);
            usageIndex++;
            usageSavedSize += batch.size();
            p("3 使用日志写入批次：" + usageIndex + " / " + usageBatches.size() + "，累计数量：" + usageSavedSize, startTime);
        }
        accountInfoService.updateBatchById(accountInfoList);

        p("充值记录生成完成", startTime);
    }

    /**
     * 积分使用记录——使用时间严格晚于账户创建时间，且在同一账户内按时间顺序递增。
     */
    @Test
    public void testPointsUsage() {
        long startTime = System.currentTimeMillis();
        p("开始生成积分使用记录，目标数量：" + TARGET_USAGE_COUNT);

        List<AccountInfo> accountInfoList = accountInfoService.list(
                new LambdaQueryWrapper<AccountInfo>()
                        .notIn(AccountInfo::getUserId, EXCLUDED_USER_IDS)
                        .notIn(AccountInfo::getAccountId, EXCLUDED_ACCOUNT_IDS));
        int totalAccounts = accountInfoList.size();
        if (totalAccounts == 0) {
            p("没有可用账户");
            return;
        }

        // 加载用户真实 IP
        List<String> userIds = accountInfoList.stream().map(AccountInfo::getUserId).toList();
        List<UserInfo> userList = userInfoService.list(
                new LambdaQueryWrapper<UserInfo>().in(UserInfo::getUserId, userIds));
        Map<String, UserInfo> userMap = userList.stream().collect(java.util.stream.Collectors.toMap(UserInfo::getUserId, u -> u));
        Map<String, String> userIpAddrMap = new HashMap<>();
        Map<String, String> userIpAddressMap = new HashMap<>();
        for (AccountInfo acc : accountInfoList) {
            UserInfo u = userMap.get(acc.getUserId());
            userIpAddrMap.put(acc.getUserId(), u != null ? u.getLastLoginIp() : RandomUtils.generateRandomIpAddr());
            userIpAddressMap.put(acc.getUserId(), u != null ? u.getIpAddress() : RandomUtils.generateRandomIPAddress());
        }

        // 计算每个账户应分配的使用记录数（前多后少）
        long[] accountAllocation = computeLinearAllocation(TARGET_USAGE_COUNT, totalAccounts);

        List<PointsUsageLogInfo> pointsUsageLogInfos = new ArrayList<>();
        Long totalCount = 100L;
        long inserted = 0;
        long lastPrint = 0;

        for (int ai = 0; ai < totalAccounts; ai++) {
            AccountInfo accountInfo = accountInfoList.get(ai);
            String accountId = accountInfo.getAccountId();
            long l = Long.parseLong(accountId) % 2 + 1;
            Date lastUsageTime = accountInfo.getCreateTime();
            int usageCount = (int) accountAllocation[ai];

            for (int i = 0; i < usageCount; i++) {
                lastUsageTime = RandomUtils.generateDateAfter(lastUsageTime, MIN_MINUTES, MAX_MINUTES);

                PointsUsageLogInfo pointsUsageLogInfo = new PointsUsageLogInfo();
                pointsUsageLogInfo.setLogId(IdUtils.snowflakeId().toString());
                pointsUsageLogInfo.setUserId(accountInfo.getUserId());
                pointsUsageLogInfo.setGiveUserId(null);
                pointsUsageLogInfo.setLogType(PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_1.getValue());
                pointsUsageLogInfo.setUsageType(String.valueOf(l));
                pointsUsageLogInfo.setTargetId(null);
                pointsUsageLogInfo.setPointsBefore(accountInfo.getPointsBalance());
                pointsUsageLogInfo.setPointsUsed(totalCount);
                accountInfo.setPointsBalance(accountInfo.getPointsBalance() + totalCount);
                pointsUsageLogInfo.setPointsAfter(accountInfo.getPointsBalance());
                pointsUsageLogInfo.setDeviceId(null);
                pointsUsageLogInfo.setBrowser("Chrome");
                pointsUsageLogInfo.setOs("Windows 11");
                pointsUsageLogInfo.setPlatform("Windows");
                pointsUsageLogInfo.setIpAddr(userIpAddrMap.get(accountInfo.getUserId()));
                pointsUsageLogInfo.setIpAddress(userIpAddressMap.get(accountInfo.getUserId()));
                pointsUsageLogInfo.setCreateTime(lastUsageTime);
                pointsUsageLogInfo.setUpdateTime(lastUsageTime);
                pointsUsageLogInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
                pointsUsageLogInfos.add(pointsUsageLogInfo);
                inserted++;
                if (inserted - lastPrint >= 100000) {
                    p("积分使用记录生成进度：" + inserted + " / " + TARGET_USAGE_COUNT + "（" + String.format("%.1f", (double) inserted / TARGET_USAGE_COUNT * 100) + "%）", startTime);
                    lastPrint = inserted;
                }
            }
        }

        List<List<PointsUsageLogInfo>> batches = partition(pointsUsageLogInfos, BATCH_SIZE);
        p("实际生成积分使用记录：" + pointsUsageLogInfos.size() + "，分 " + batches.size() + " 批，开始写入", startTime);

        int index = 0;
        int savedSize = 0;
        for (List<PointsUsageLogInfo> batch : batches) {
            pointsUsageLogInfoService.saveBatch(batch);
            index++;
            savedSize += batch.size();
            p("积分使用记录写入批次：" + index + " / " + batches.size() + "，累计数量：" + savedSize, startTime);
        }

        accountInfoService.updateBatchById(accountInfoList);
        p("账户信息更新完成，共 " + accountInfoList.size() + " 条", startTime);

    }

    // ==================== 辅助方法 ====================

    /**
     * 按线性比例分配总数到各账户。
     * 越靠前的账户分配的越多，总和保证恰好等于 target。
     */
    private long[] computeLinearAllocation(long target, int count) {
        if (count <= 0) {
            return new long[0];
        }
        long[] result = new long[count];
        long totalWeight = 0;
        for (int i = 0; i < count; i++) {
            totalWeight += (count - i);
        }
        long sum = 0;
        for (int i = 0; i < count; i++) {
            long allocation = Math.max(1, (long) Math.round((double) (count - i) / totalWeight * target));
            result[i] = allocation;
            sum += allocation;
        }
        long diff = target - sum;
        int idx = 0;
        while (diff != 0) {
            result[idx % count] += (diff > 0 ? 1 : -1);
            diff += (diff > 0 ? -1 : 1);
            idx++;
        }
        return result;
    }

    private <T> List<List<T>> partition(List<T> list, int batchSize) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i += batchSize) {
            result.add(list.subList(i, Math.min(i + batchSize, list.size())));
        }
        return result;
    }

    private void waitFutures(List<Future<?>> futures) {
        for (Future<?> f : futures) {
            try {
                f.get(30, TimeUnit.MINUTES);
            } catch (Exception e) {
                System.err.println("执行异常：" + e.getMessage());
            }
        }
    }

    private void p(String msg, long startTime) {
        long elapsedMillis = System.currentTimeMillis() - startTime;
        long minutes = elapsedMillis / 60000;
        long seconds = (elapsedMillis % 60000) / 1000;
        System.out.printf("[%d分%d秒] %s%n", minutes, seconds, msg);
    }

    private void p(String msg) {
        System.out.println(msg);
    }

    // ==================== 清理方法 ====================

    @Test
    public void testDeleteStatistics() {
        poStatisticsInfoService.remove(new LambdaQueryWrapper<>());
    }

    @Test
    public void testDeletePointsGenerate() {
        long start = System.currentTimeMillis();
        int batchSize = BATCH_SIZE;
        p("开始删除积分生成记录", start);
        // 删除积分使用
        long totalUsageDeleted = 0;
        int usageLoopCount = 0;
        while (true) {
            boolean remove = pointsUsageLogInfoService.remove(
                    new LambdaQueryWrapper<PointsUsageLogInfo>()
                            .notIn(PointsUsageLogInfo::getUserId, EXCLUDED_USER_IDS)
                            .orderByAsc(PointsUsageLogInfo::getLogId)
                            .last("LIMIT " + batchSize));
            if (!remove) break;
            totalUsageDeleted += batchSize;
            usageLoopCount++;
            p("[1/4] 删除积分使用记录批次 " + usageLoopCount + "：累计删 " + totalUsageDeleted + " 条", start);
        }
        p("[1/4] 删除积分使用记录完成，共 " + totalUsageDeleted + " 条", start);

        // 删除积分充值
        long totalRechargeDeleted = 0;
        int rechargeLoopCount = 0;
        while (true) {
            boolean remove = pointsRechargeInfoService.remove(
                    new LambdaQueryWrapper<PointsRechargeInfo>()
                            .notIn(PointsRechargeInfo::getUserId, EXCLUDED_USER_IDS)
                            .orderByAsc(PointsRechargeInfo::getRechargeId)
                            .last("LIMIT " + batchSize));
            if (!remove) break;
            totalRechargeDeleted += batchSize;
            rechargeLoopCount++;
            p("[2/4] 删除积分充值记录批次 " + rechargeLoopCount + "：累计删 " + totalRechargeDeleted + " 条", start);
        }
        p("[2/4] 删除积分充值记录完成，共 " + totalRechargeDeleted + " 条", start);

        // 删除充值
        long totalOrderDeleted = 0;
        int orderLoopCount = 0;
        while (true) {
            boolean remove = paymentOrderInfoService.remove(
                    new LambdaQueryWrapper<PaymentOrderInfo>()
                            .notIn(PaymentOrderInfo::getUserId, EXCLUDED_USER_IDS)
                            .orderByAsc(PaymentOrderInfo::getOrderId)
                            .last("LIMIT " + batchSize));
            if (!remove) break;
            totalOrderDeleted += batchSize;
            orderLoopCount++;
            p("[3/4] 删除充值订单批次 " + orderLoopCount + "：累计删 " + totalOrderDeleted + " 条", start);
        }
        p("[3/4] 删除充值订单完成，共 " + totalOrderDeleted + " 条", start);

        // 删除账户信息
        long totalAccountDeleted = 0;
        int accountLoopCount = 0;
        while (true) {
            boolean remove = accountInfoService.remove(
                    new LambdaQueryWrapper<AccountInfo>()
                            .notIn(AccountInfo::getUserId, EXCLUDED_USER_IDS)
                            .orderByAsc(AccountInfo::getAccountId)
                            .last("LIMIT " + batchSize));
            if (!remove) break;
            totalAccountDeleted += batchSize;
            accountLoopCount++;
            p("[4/4] 删除账户信息批次 " + accountLoopCount + "：累计删 " + totalAccountDeleted + " 条", start);
        }
        p("[4/4] 删除账户信息完成，共 " + totalAccountDeleted + " 条", start);
        p("删除完成", start);
    }
}
