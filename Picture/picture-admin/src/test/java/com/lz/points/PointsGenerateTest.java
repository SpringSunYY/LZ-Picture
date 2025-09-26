package com.lz.points;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.points.model.domain.AccountInfo;
import com.lz.points.model.enums.PoAccountStatusEnum;
import com.lz.points.service.IAccountInfoService;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.service.IUserInfoService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
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
}
