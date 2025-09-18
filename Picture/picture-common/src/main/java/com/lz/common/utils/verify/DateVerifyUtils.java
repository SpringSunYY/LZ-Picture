package com.lz.common.utils.verify;

import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;

import java.util.Date;

/**
 * 时间校验类
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-18  16:18
 * @Version: 1.0
 */
public class DateVerifyUtils {
    /**
     * 校验时间是否在允许范围内
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     */
    public static Date checkDateIsStartAfter(String startDate, String endDate) {
        //如果开始时间大于结束时间
        Date startTime = DateUtils.parseDate(startDate);
        Date endTime = DateUtils.parseDate(endDate);
        if (startTime.getTime() > endTime.getTime()) {
            throw new ServiceException("开始时间不能大于结束时间");
        }
        //如果结束时间大于当前时间
        Date nowDate = DateUtils.getNowDate();
        if (endTime.getTime() > nowDate.getTime()) {
            throw new ServiceException("结束时间不能大于当前时间");
        }
        return nowDate;
    }
}
