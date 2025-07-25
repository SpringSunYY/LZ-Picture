package com.lz.quartz.task;

import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.points.service.IPaymentOrderInfoService;
import com.lz.points.service.IPointsAutoTask;
import com.lz.points.service.IPointsRechargeInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 积分定时任务
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-19  10:13
 * @Version: 1.0
 */
@Component("pointsTask")
public class PointsTask {

    @Resource
    private IPointsAutoTask pointsAutoTask;

    public void autoUpdateExpiredOrder(Integer minutes) {
        if (StringUtils.isNull(minutes)) {
            minutes = 5;
        }
        Date date = new Date();
        Date expiredTime = DateUtils.addMinutes(date, -minutes);
        pointsAutoTask.autoUpdateExpiredOrder(expiredTime);
        pointsAutoTask.autoUpdateRechargeExpiredOrder(expiredTime);
    }

    public void autoUpdatePointsRechargePackageInfo() {
        pointsAutoTask.autoUpdatePointsRechargePackageInfo();
    }
}
