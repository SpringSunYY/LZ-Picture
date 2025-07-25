package com.lz.common;

import com.alibaba.fastjson2.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.lz.common.utils.DateUtils;
import com.lz.config.manager.sms.SmsManager;
import com.lz.config.manager.sms.model.SmsBody;
import com.lz.picture.model.enums.PStatisticsTypeEnum;
import com.lz.picture.service.IStatisticsInfoService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import static com.lz.picture.utils.PictureStatisticsUtil.PICTURE_STATISTICS_PICTURE_HOT;
import static com.lz.picture.utils.PictureStatisticsUtil.PICTURE_STATISTICS_PICTURE_HOT_TOTAL_KEY;

/**
 * Project: Picture
 * Package: com.lz.common
 * Author: YY
 * CreateTime: 2025-03-19  11:41
 * Description: CommonTest
 * Version: 1.0
 */
@SpringBootTest
public class CommonTest {
    @Resource
    private SmsManager smsManage;
    @Resource
    private IStatisticsInfoService statisticsInfoService;

    @Test
    public void testSendSms() {
        SmsBody smsBody = new SmsBody();
//        smsBody.setCode("123456");
        String jsonString = JSONObject.toJSONString(smsBody);
        SendSmsResponse response = smsManage.sendSms("18585595238",
                "荔枝开发阶段短信服务",
                "SMS_480850068", jsonString
        );
        System.out.println("短信接口返回的数据----------------");
        System.out.println("Code=" + response.getCode());
        System.out.println("Message=" + response.getMessage());
        System.out.println("RequestId=" + response.getRequestId());
        System.out.println("BizId=" + response.getBizId());
    }

    @Test
    public void getWeekStartAndEndTime() {
        // 获取指定时间的周一和周日
        LocalDate specifiedDate = LocalDate.of(2025, 3, 19);
        // 获取该日期所在周的周一
        LocalDate mondayOfTheWeek = specifiedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        // 获取该日期所在周的周日
        LocalDate sundayOfTheWeek = specifiedDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // 周一的开始时间（00:00）
        LocalDateTime weekStart = mondayOfTheWeek.atStartOfDay();
        // 周日的结束时间（23:59）
        LocalDateTime weekEnd = sundayOfTheWeek.atTime(LocalTime.MAX);

        // 格式化输出
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("周一开始时间：" + weekStart.format(formatter));
        System.out.println("周末结束时间：" + weekEnd.format(formatter));

        Date nowDate = DateUtils.getNowDate();
        String weekDay = DateUtils.getWeekDay(nowDate, 1, "yyyy-MM-dd");
        System.out.println(weekDay);
        System.out.println(DateUtils.getWeekDay(nowDate, 2, "yyyy-MM-dd"));
        System.out.println(DateUtils.getWeekDay(nowDate, 3, "yyyy-MM-dd"));
        System.out.println(DateUtils.getWeekDay(nowDate, 4, "yyyy-MM-dd"));
        System.out.println(DateUtils.getMonthDay(nowDate, 5, "yyyy-MM-dd"));
        System.out.println(DateUtils.getYearDay(nowDate, 5, "yyyy-MM-dd"));
    }

    @Test
    public void testDownloadPictureHot() {
        statisticsInfoService.getStatisticsPictureHotFilePath(PStatisticsTypeEnum.STATISTICS_TYPE_6.getValue(), PICTURE_STATISTICS_PICTURE_HOT, PICTURE_STATISTICS_PICTURE_HOT_TOTAL_KEY, 1L, 10);
    }
}
