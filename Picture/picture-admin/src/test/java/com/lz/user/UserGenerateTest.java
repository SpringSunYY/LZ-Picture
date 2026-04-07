package com.lz.user;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.enums.ULoginStatusEnum;
import com.lz.common.enums.ULoginTypeEnum;
import com.lz.common.enums.UUserStatusEnum;
import com.lz.common.utils.RandomUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.config.model.domain.InformTemplateInfo;
import com.lz.config.service.IInformTemplateInfoService;
import com.lz.user.model.domain.InformInfo;
import com.lz.user.model.domain.LoginLogInfo;
import com.lz.user.model.domain.UStatisticsInfo;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.model.enums.UInformIsReadEnum;
import com.lz.user.model.enums.UInformStatusEnum;
import com.lz.user.model.enums.UInformTypeEnum;
import com.lz.user.service.IInformInfoService;
import com.lz.user.service.ILoginLogInfoService;
import com.lz.user.service.IUStatisticsInfoService;
import com.lz.user.service.IUserInfoService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

/**
 * 用户生成数据
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-14  15:30
 * @Version: 1.0
 */
@SpringBootTest()
public class UserGenerateTest {
    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private ILoginLogInfoService loginLogInfoService;

    @Resource
    private IInformTemplateInfoService informTemplateInfoService;

    @Resource
    private IInformInfoService informInfoService;

    @Resource
    private IUStatisticsInfoService statisticsInfoService;

    // ==================== 全局可配置参数 ====================

    /**
     * 不需要生成数据的用户 ID 列表
     */
    private static final List<String> EXCLUDED_USER_IDS = List.of("1", "2", "009");

    /**
     * 用户总数 = TOTAL_BATCHES × USERS_PER_BATCH
     */
    private static final int TOTAL_BATCHES = 10;

    /**
     * 每批用户数
     */
    private static final int USERS_PER_BATCH = 10000;

    /**
     * 比指定时间晚的最小分钟数
     */
    private static final int MIN_MINUTES = 1;

    /**
     * 比指定时间按晚的最大分钟数
     */
    private static final int MAX_MINUTES = 720;

    /**
     * 每个用户生成的登录日志条数
     */
    private static final int LOGIN_RECORDS_PER_USER = 15;

    /**
     * 每个用户每个通知模板最多生成的通知条数
     */
    private static final int MAX_INFORM_PER_TEMPLATE = 1;

    // ==================== 静态数据 ====================

    private static final List<String> OCCUPATION_LIST = List.of(
            "Java开发工程师", "Python开发工程师", "C++开发工程师", "大学老师", "学生",
            "公务员", "运维工程师", "项目经理", "数据分析师", "测试工程师",
            "产品经理", "UI设计师", "UX设计师", "数据科学家", "软件架构师",
            "网络工程师", "硬件工程师", "软件测试师", "软件工程师",
            "软件开发工程师", "软件测试工程师", "软件测试经理", "软件测试总监"
    );

    // ==================== 数据生成方法 ====================

    @Test
    public void testGenerate() {
        long startTime = System.currentTimeMillis();
        p("开始生成数据...");
        generateUser();
        p("生成用户完成", startTime);
        generateLoginLog();
        p("生成登录日志完成", startTime);
        generateInform();
        p("生成消息完成", startTime);
        p("数据生成完成", startTime);
    }

    /**
     * 生成用户——注册时间呈现增长趋势。
     * 通过 TOTAL_BATCHES 和 USERS_PER_BATCH 控制总数。
     * 整体模拟平台从2025年初到2026年初用户量自然增长的过程。
     * 前期注册量少、间隔稀疏；后期注册量密集、持续涌入。
     */
    @Test
    public void generateUser() {
        long startTime = System.currentTimeMillis();
        int totalUsers = TOTAL_BATCHES * USERS_PER_BATCH;
        p("开始生成用户，目标总数：" + totalUsers + "（" + TOTAL_BATCHES + " 批 × " + USERS_PER_BATCH + " 人）");

        HashMap<Integer, List<UserInfo>> userMap = new LinkedHashMap<>();

        for (int i = 0; i < TOTAL_BATCHES; i++) {
            ArrayList<UserInfo> value = new ArrayList<>();
            for (int j = 0; j < USERS_PER_BATCH; j++) {
                int globalIndex = i * USERS_PER_BATCH + j;
                Date regTime = RandomUtils.generateGrowthTrendDate(2025, 2026, globalIndex, totalUsers, 0.7);

                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(IdUtils.snowflakeId().toString());
                String userName = "LZ-Picture_" + i + "_" + j;
                userInfo.setUserName(userName);
                userInfo.setPhone(RandomUtils.generateChinesePhoneNumber());
                userInfo.setCountryCode("+86");
                userInfo.setNickName(userName);
                userInfo.setAvatarUrl("/picture/avatar/2025/08/17/懒羊羊 - 32-1957000313210802176-compressed.jpg");
                userInfo.setStatus(UUserStatusEnum.USER_STATUS_0.getValue());
                userInfo.setPassword("917d445b77b7699dca03102a04f89519");
                userInfo.setSalt("md5");
                String sex = j % 3 == 0 ? "1" : "2";
                userInfo.setSex(sex);
                userInfo.setBirthday(RandomUtils.generateDate(1945, 2024));
                userInfo.setOccupation(OCCUPATION_LIST.get(j % OCCUPATION_LIST.size()));
                userInfo.setPreferredLanguageLocale("zh-CN");
                userInfo.setIntroductory("测试生成");
                userInfo.setIpAddress(RandomUtils.generateRandomIPAddress());
                userInfo.setCreateTime(regTime);
                userInfo.setUpdateTime(regTime);
                userInfo.setLastLoginTime(RandomUtils.generateDateAfter(regTime, 1, MAX_MINUTES));
                userInfo.setLastLoginIp(RandomUtils.generateRandomIpAddr());
                userInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
                value.add(userInfo);
            }
            userMap.put(i, value);
            if ((i + 1) % 10 == 0 || i == TOTAL_BATCHES - 1) {
                long progress = (long) (i + 1) * USERS_PER_BATCH;
                p("用户生成进度：" + progress + " / " + totalUsers + "（" + String.format("%.1f", (double) progress / totalUsers * 100) + "%）", startTime);
            }
        }

        int savedSize = 0;
        for (Map.Entry<Integer, List<UserInfo>> entry : userMap.entrySet()) {
            Integer k = entry.getKey();
            List<UserInfo> v = entry.getValue();
            userInfoService.saveBatch(v);
            savedSize += v.size();
            p("用户生成进度：" + savedSize + " / " + totalUsers + "（" + String.format("%.1f", (double) savedSize / totalUsers * 100) + "%）", startTime);
        }
        p("实际生成用户：" + totalUsers + "，开始写入", startTime);
        p("用户生成完成", startTime);
    }

    /**
     * 生成登录日志——登录时间具有周期性分布，且严格晚于用户注册时间。
     * 每个用户生成 LOGIN_RECORDS_PER_USER 条登录记录，
     * 登录时间均匀分布在注册之后，顺序递增，落在更接近真实用户习惯的时段。
     */
    @Test
    public void generateLoginLog() {
        long startTime = System.currentTimeMillis();
        p("开始生成登录日志，每用户 " + LOGIN_RECORDS_PER_USER + " 条...");

        List<UserInfo> userInfoList = userInfoService.list(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                .notIn(UserInfo::getUserId, EXCLUDED_USER_IDS));

        // 预生成每个用户的固定 IP，后续该用户所有登录记录复用
        Map<String, String> userIpAddrMap = new HashMap<>();
        Map<String, String> userIpAddressMap = new HashMap<>();
        for (UserInfo u : userInfoList) {
            userIpAddrMap.put(u.getUserId(), RandomUtils.generateRandomIpAddr());
            userIpAddressMap.put(u.getUserId(), RandomUtils.generateRandomIPAddress());
        }

        // 记录每个用户的最后登录时间和 IP（用于回写 user_info 表）
        Map<String, UserInfo> lastLoginMap = new HashMap<>();

        HashMap<String, List<LoginLogInfo>> loginLogMap = new LinkedHashMap<>();
        int index = 0;
        long lastPrint = 0;
        int totalUsers = userInfoList.size();

        for (UserInfo userInfo : userInfoList) {
            ArrayList<LoginLogInfo> loginLogInfos = new ArrayList<>();
            Date lastLoginTime = userInfo.getCreateTime();

            for (int j = 0; j < LOGIN_RECORDS_PER_USER; j++) {
                lastLoginTime = RandomUtils.generateDateAfter(lastLoginTime, MIN_MINUTES, MAX_MINUTES);

                LoginLogInfo loginLogInfo = new LoginLogInfo();
                loginLogInfo.setInfoId(IdUtils.fastSimpleUUID());
                loginLogInfo.setUserId(userInfo.getUserId());
                loginLogInfo.setUserName(userInfo.getUserName());
                loginLogInfo.setLoginType(ULoginTypeEnum.LOGIN_TYPE_0.getValue());
                loginLogInfo.setIdentifier(null);
                loginLogInfo.setIpaddr(RandomUtils.generateRandomIpAddr());
                loginLogInfo.setLoginLocation(RandomUtils.generateRandomIPAddress());
                loginLogInfo.setBrowser("Firefox 14");
                loginLogInfo.setOs("Windows 10");
                loginLogInfo.setPlatform("Windows");
                loginLogInfo.setDeviceId(null);
                loginLogInfo.setStatus(ULoginStatusEnum.LOGIN_STATUS_0.getValue());
                loginLogInfo.setErrorCode(null);
                loginLogInfo.setMsg(null);
                loginLogInfo.setLoginTime(lastLoginTime);
                loginLogInfos.add(loginLogInfo);
            }

            // 记录该用户最后一条登录的时间和 IP，用于回写 user_info 表
            UserInfo updateInfo = new UserInfo();
            updateInfo.setUserId(userInfo.getUserId());
            LoginLogInfo lastLog = loginLogInfos.getLast();
            updateInfo.setLastLoginTime(lastLog.getLoginTime());
            updateInfo.setLastLoginIp(lastLog.getIpaddr());
            lastLoginMap.put(userInfo.getUserId(), updateInfo);

            loginLogMap.put(userInfo.getUserId(), loginLogInfos);

            index++;
            if (index - lastPrint >= 1000) {
                p("登录日志生成进度：" + index + " / " + totalUsers + "（" + String.format("%.1f", (double) index / totalUsers * 100) + "%）", startTime);
                lastPrint = index;
            }
        }

        int savedIndex = 0;
        int savedSize = 0;
        List<LoginLogInfo> batchList = new ArrayList<>();
        int userCount = 0;

        for (String userId : loginLogMap.keySet()) {
            List<LoginLogInfo> loginLogInfos = loginLogMap.get(userId);
            batchList.addAll(loginLogInfos);
            userCount++;
            if (userCount >= USERS_PER_BATCH) {
                loginLogInfoService.saveBatch(batchList);
                savedIndex++;
                savedSize += batchList.size();
                p("当前生成登录日志批次：" + savedIndex + ",数量：" + savedSize, startTime);
                p("登录日志生成进度：" + savedIndex * USERS_PER_BATCH + " / " + totalUsers + "（" + String.format("%.1f", (double) savedIndex * USERS_PER_BATCH / totalUsers * 100) + "%）", startTime);
                batchList.clear();
                userCount = 0;
            }
        }
        if (!batchList.isEmpty()) {
            loginLogInfoService.saveBatch(batchList);
            savedIndex++;
            savedSize += batchList.size();
        }
        p("当前生成登录日志批次：" + savedIndex + ",数量：" + savedSize, startTime);
        p("登录日志生成进度：" + savedIndex * USERS_PER_BATCH + " / " + totalUsers + "（" + String.format("%.1f", (double) savedIndex * USERS_PER_BATCH / totalUsers * 100) + "%）", startTime);
        // 回写所有用户的最后登录时间和 IP
        userInfoService.updateBatchById(new ArrayList<>(lastLoginMap.values()));
        p("登录日志生成完毕，共 " + savedIndex + " 用户、" + (savedIndex * LOGIN_RECORDS_PER_USER) + " 条日志，开始写入", startTime);

        // 回写所有用户的最后登录时间和 IP
        userInfoService.updateBatchById(new ArrayList<>(lastLoginMap.values()));

        p("登录日志写入完成", startTime);
    }

    /**
     * 生成通知消息——发送时间必须在用户注册之后。
     * 每个用户对每个通知模板随机生成 0~1 条通知，时间晚于该用户的注册时间。
     */
    @Test
    public void generateInform() {
        long startTime = System.currentTimeMillis();
        p("开始生成通知消息...");

        List<UserInfo> userInfoList = userInfoService.list(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                .notIn(UserInfo::getUserId, EXCLUDED_USER_IDS));
        List<InformTemplateInfo> informTemplateInfos = informTemplateInfoService.list(
                new LambdaQueryWrapper<InformTemplateInfo>());

        HashMap<String, List<InformInfo>> informMap = new LinkedHashMap<>();
        long totalCount = 0;
        int userIndex = 0;

        for (UserInfo userInfo : userInfoList) {
            ArrayList<InformInfo> informInfos = new ArrayList<>();
            Date userRegTime = userInfo.getCreateTime();

            for (InformTemplateInfo informTemplateInfo : informTemplateInfos) {
                if (StringUtils.isEmpty(informTemplateInfo.getInformTitle())) {
                    continue;
                }
                Random random = new Random();
                int count = random.nextInt(MAX_INFORM_PER_TEMPLATE + 1);
                for (int j = 0; j < count; j++) {
                    Date sendTime = RandomUtils.generateDateAfter(userRegTime, MIN_MINUTES, MAX_MINUTES);

                    InformInfo informInfo = new InformInfo();
                    informInfo.setRecordId(IdUtils.fastSimpleUUID());
                    informInfo.setTemplateKey(informTemplateInfo.getTemplateKey());
                    informInfo.setTemplateType(informTemplateInfo.getTemplateType());
                    informInfo.setLocale(informTemplateInfo.getLocale());
                    informInfo.setInformType(UInformTypeEnum.INFORM_TYPE_0.getValue());
                    informInfo.setInformTitle(informTemplateInfo.getInformTitle());
                    informInfo.setStatus(UInformStatusEnum.INFORM_STATUS_1.getValue());
                    informInfo.setContent(informTemplateInfo.getContent());
                    informInfo.setUserId(userInfo.getUserId());
                    informInfo.setIsRead(UInformIsReadEnum.INFORM_IS_READ_0.getValue());
                    informInfo.setRetryCount(0L);
                    informInfo.setSendTime(sendTime);
                    informInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
                    informInfos.add(informInfo);
                    totalCount++;
                }
            }
            informMap.put(userInfo.getUserId(), informInfos);

            userIndex++;
            if (userIndex % 500 == 0) {
                System.err.println("用户进度: " + userIndex);
            }
        }
        p("总条数: " + totalCount);

        int index = 0;
        int savedSize = 0;
        List<InformInfo> batchList = new ArrayList<>();
        int userCount = 0;

        for (String id : informMap.keySet()) {
            List<InformInfo> informInfos = informMap.get(id);
            if (informInfos.isEmpty()) {
                continue;
            }
            batchList.addAll(informInfos);
            userCount++;
            if (userCount >= USERS_PER_BATCH) {
                informInfoService.saveBatch(batchList);
                index++;
                savedSize += batchList.size();
                p("当前生成消息批次：" + index + ",数量：" + savedSize, startTime);
                p("生成消息进度：" + savedSize + " / " + totalCount + "（" + String.format("%.1f", (double) savedSize / totalCount * 100) + "%）", startTime);

                batchList.clear();
                userCount = 0;
            }
        }
        if (!batchList.isEmpty()) {
            informInfoService.saveBatch(batchList);
            index++;
            savedSize += batchList.size();
        }
        p("当前生成消息批次：" + index + ",数量：" + savedSize, startTime);
        p("生成消息进度：" + savedSize + " / " + totalCount + "（" + String.format("%.1f", (double) savedSize / totalCount * 100) + "%）", startTime);
    }

    // ==================== 清理方法 ====================

    /**
     * 删除统计
     */
    @Test
    public void testDeleteStatisticsGenerate() {
        statisticsInfoService.remove(new LambdaQueryWrapper<UStatisticsInfo>());
    }

    @Test
    public void testDeleteUserGenerate() {
        testDeleteStatisticsGenerate();
        long start = System.currentTimeMillis();
        int batchSize = 300000;

        // 删除信息（游标分页）
        long totalRecordDeleted = 0;
        int recordLoopCount = 0;
        while (true) {
            boolean remove = informInfoService.remove(
                    new LambdaQueryWrapper<InformInfo>()
                            .notIn(InformInfo::getUserId, EXCLUDED_USER_IDS)
                            .orderByAsc(InformInfo::getRecordId)
                            .last("LIMIT " + batchSize));
            if (!remove) break;
            totalRecordDeleted += batchSize;
            recordLoopCount++;
            p("[1/3] 删除通知消息批次 " + recordLoopCount + "：累计删 " + totalRecordDeleted + " 条", start);
        }
        p("[1/3] 删除通知消息完成，共 " + totalRecordDeleted + " 条", start);

        // 删除登录日志（游标分页）
        long totalLoginDeleted = 0;
        int loginLoopCount = 0;
        while (true) {
            boolean remove = loginLogInfoService.remove(
                    new LambdaQueryWrapper<LoginLogInfo>()
                            .notIn(LoginLogInfo::getUserId, EXCLUDED_USER_IDS)
                            .orderByAsc(LoginLogInfo::getInfoId)
                            .last("LIMIT " + batchSize));
            if (!remove) break;
            totalLoginDeleted += batchSize;
            loginLoopCount++;
            p("[2/3] 删除登录日志批次 " + loginLoopCount + "：累计删 " + totalLoginDeleted + " 条", start);
        }
        p("[2/3] 删除登录日志完成，共 " + totalLoginDeleted + " 条", start);

        // 删除用户
        long totalUserDeleted = 0;
        int userLoopCount = 0;
        while (true) {
            boolean remove = userInfoService.remove(
                    new LambdaQueryWrapper<UserInfo>()
                            .notIn(UserInfo::getUserId, EXCLUDED_USER_IDS)
                            .orderByAsc(UserInfo::getUserId)
                            .last("LIMIT " + batchSize));
            if (!remove) break;
            totalUserDeleted += batchSize;
            userLoopCount++;
            p("[3/3] 删除用户批次 " + userLoopCount + "：累计删 " + totalUserDeleted + " 个", start);
        }
        p("[3/3] 删除用户完成，共 " + totalUserDeleted + " 个", start);
        p("删除完成", start);
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
}
