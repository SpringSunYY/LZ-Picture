package com.lz.user;


import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.enums.UUserStatusEnum;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.RandomUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.service.IUserInfoService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    private TransactionTemplate transactionTemplate;

    @Test
    public void generateUser() {
        HashMap<Integer, List<UserInfo>> userMap = new HashMap<>();
        List<String> occupationList = List.of("Java开发工程师", "Python开发工程师", "C++开发工程师", "大学老师", "学生", "公务员", "运维工程师", "项目经理", "数据分析师", "测试工程师", "产品经理", "UI设计师", "UX设计师", "数据科学家", "软件架构师", "网络工程师", "硬件工程师", "软件测试师", "软件工程师", "软件开发工程师", "软件测试工程师", "软件测试经理", "软件测试总监", "软件测试经理", "软件测试总监", "软件测试经理", "软件测试总监", "软件测试经理", "软件测试总监", "软件测试经理", "软件测试总监", "软件测试经理", "软件测试总监");
        for (int i = 0; i < 100; i++) {
            ArrayList<UserInfo> value = new ArrayList<>();
            for (int j = 0; j < 1000; j++) {
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
                int i1 = j % 3;
                userInfo.setSex(String.valueOf(i1));
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
    public void generateDate() {
        for (int i = 0; i < 100; i++) {
            Date date = RandomUtils.generateDate(2025, 2025);
            System.out.println("DateUtils.dateTime( date) = " + DateUtils.dateTime(date));
        }
    }
}
