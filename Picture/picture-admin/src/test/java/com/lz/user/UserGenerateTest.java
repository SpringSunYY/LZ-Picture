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
import com.lz.user.model.domain.UserInfo;
import com.lz.user.model.enums.UInformIsReadEnum;
import com.lz.user.model.enums.UInformStatusEnum;
import com.lz.user.model.enums.UInformTypeEnum;
import com.lz.user.service.IInformInfoService;
import com.lz.user.service.ILoginLogInfoService;
import com.lz.user.service.IUserInfoService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.concurrent.Future;

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
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private IInformTemplateInfoService informTemplateInfoService;

    @Resource
    private IInformInfoService informInfoService;

    @Test
    public void generateUser() {
        HashMap<Integer, List<UserInfo>> userMap = new HashMap<>();
        List<String> occupationList = List.of("Java开发工程师", "Python开发工程师", "C++开发工程师", "大学老师", "学生", "公务员", "运维工程师", "项目经理", "数据分析师", "测试工程师", "产品经理", "UI设计师", "UX设计师", "数据科学家", "软件架构师", "网络工程师", "硬件工程师", "软件测试师", "软件工程师", "软件开发工程师", "软件测试工程师", "软件测试经理", "软件测试总监", "软件测试经理", "软件测试总监", "软件测试经理", "软件测试总监", "软件测试经理", "软件测试总监", "软件测试经理", "软件测试总监", "软件测试经理", "软件测试总监");
        for (int i = 0; i < 100; i++) {
            ArrayList<UserInfo> value = new ArrayList<>();
            for (int j = 0; j < 3000; j++) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(IdUtils.snowflakeId().toString());
                String userName = "LZ-Picture_3_" + i + "_" + j;
                userInfo.setUserName(userName);
                userInfo.setPhone(RandomUtils.generateChinesePhoneNumber());
                userInfo.setCountryCode("+86");
                userInfo.setNickName(userName);
                userInfo.setAvatarUrl("/picture/avatar/2025/08/17/懒羊羊 - 32-1957000313210802176-compressed.jpg");
                userInfo.setStatus(UUserStatusEnum.USER_STATUS_0.getValue());
                userInfo.setPassword("917d445b77b7699dca03102a04f89519");
                userInfo.setSalt("md5");
                String sex = j % 2 == 0 ? "1" : "2";
                userInfo.setSex(sex);
                userInfo.setBirthday(RandomUtils.generateDate(1945, 2024));
                userInfo.setOccupation(occupationList.get(j % occupationList.size()));
                userInfo.setPreferredLanguageLocale("zh-CN");
                userInfo.setIntroductory("测试生成");
                Date time = RandomUtils.generateDate(2025, 2025);
                userInfo.setIpAddress(RandomUtils.generateRandomIPAddress());
                userInfo.setLastLoginTime(time);
                userInfo.setLastLoginIp(RandomUtils.generateRandomIpAddr());
                userInfo.setCreateTime(time);
                userInfo.setUpdateTime(time);
                userInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
//                userInfo.setParams();
                value.add(userInfo);
            }
            userMap.put(i, value);
        }
        transactionTemplate.executeWithoutResult(status -> {
            userMap.forEach((k, v) -> {
                userInfoService.saveBatch(v);
            });
        });
        userMap.forEach((k, v) -> {
            System.err.println("k = " + k);
            System.out.println("v = " + v);
        });
    }

    @Test
    public void generateLoginLog() {
        List<UserInfo> userInfoList = userInfoService.list(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                .orderByDesc(UserInfo::getCreateTime)
                .last("limit 10000"));
        HashMap<String, List<LoginLogInfo>> loginLogMap = new HashMap<>();
        for (UserInfo userInfo : userInfoList) {
            ArrayList<LoginLogInfo> loginLogInfos = new ArrayList<>();
            for (int j = 0; j < 1000; j++) {
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
                loginLogInfo.setLoginTime(RandomUtils.generateDate(2025, 2025));
                loginLogInfos.add(loginLogInfo);
            }
            loginLogMap.put(userInfo.getUserId(), loginLogInfos);
        }
        List<Future<Boolean>> futures = new ArrayList<>();
        int index = 0;
        for (String userId : loginLogMap.keySet()) {
            List<LoginLogInfo> loginLogInfos = loginLogMap.get(userId);
            futures.add(threadPoolTaskExecutor.submit(() -> {
                return loginLogInfoService.saveBatch(loginLogInfos);
            }));
            index++;
            System.out.println("index = " + index);
        }
        for (Future<Boolean> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                System.out.println("e = " + e.getMessage());
            }
        }
        threadPoolTaskExecutor.shutdown();
    }

    @Test
    public void generateInform() {
        List<UserInfo> userInfoList = userInfoService.list(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                .orderByDesc(UserInfo::getCreateTime)
                .last("limit 10000"));
        List<InformTemplateInfo> informTemplateInfos = informTemplateInfoService.list(new LambdaQueryWrapper<InformTemplateInfo>());
        HashMap<String, List<InformInfo>> informMap = new HashMap<>();
        Long size = 0L;
        for (UserInfo userInfo : userInfoList) {
            ArrayList<InformInfo> informInfos = new ArrayList<>();
            for (InformTemplateInfo informTemplateInfo : informTemplateInfos) {
                if (StringUtils.isEmpty(informTemplateInfo.getInformTitle())) {
                    continue;
                }
                Random random = new Random();
                int i = random.nextInt(10);
                for (int j = 0; j < i; j++) {
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
                    informInfo.setSendTime(RandomUtils.generateDate(2025, 2025));
                    informInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
                    informInfos.add(informInfo);
                    size++;
                }
            }
            informMap.put(userInfo.getUserId(), informInfos);
        }
        System.out.println("size = " + size);
        int index = 0;
        List<Future<Boolean>> futures = new ArrayList<>();
        for (String id : informMap.keySet()) {
            index++;
            System.err.println(index);
            List<InformInfo> informInfos = informMap.get(id);
            futures.add(threadPoolTaskExecutor.submit(() -> {
                return informInfoService.saveBatch(informInfos);
            }));
        }
        for (Future<Boolean> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                System.out.println("e = " + e.getMessage());
            }
        }
    }

    @Test
    public void generateDate() {
        long start = System.currentTimeMillis();
        int result = 0;
        for (int i = 0; i < 10000; i++) {
            System.err.println("i = " + i);
            for (int j = 0; j < 10000; j++) {
//                System.out.println("j = " + j);
                if (i % 2 == 0) {
                    result = i * j;
                } else if (i % 3 == 0) {
                    result = i - j;
                } else {
                    result = i + j;
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("result = " + result);
        System.out.println("end - start = " + (end - start));
    }

    @Test
    public void testDeleteUserGenerate(){
        List<String> userIds = new ArrayList<>();
        userIds.add("1");
        userIds.add("2");
        userIds.add("009");
        //删除信息
        informInfoService.remove(new LambdaQueryWrapper<InformInfo>().notIn(InformInfo::getUserId, userIds));
        //删除登录日志
        loginLogInfoService.remove(new LambdaQueryWrapper<LoginLogInfo>().notIn(LoginLogInfo::getUserId, userIds));
        //删除用户
        userInfoService.remove(new LambdaQueryWrapper<UserInfo>().notIn(UserInfo::getUserId, userIds));
    }
}
