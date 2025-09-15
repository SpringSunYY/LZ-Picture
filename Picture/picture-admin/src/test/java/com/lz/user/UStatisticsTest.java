package com.lz.user;

import com.lz.common.core.domain.statistics.ro.StatisticsRo;
import com.lz.common.core.domain.statistics.vo.LineStatisticsVo;
import com.lz.common.core.domain.statistics.vo.MapStatisticsVo;
import com.lz.common.core.domain.statistics.vo.PieStatisticsVo;
import com.lz.common.core.domain.statistics.vo.RadarStatisticsVo;
import com.lz.common.utils.DateUtils;
import com.lz.user.mapper.UStatisticsInfoMapper;
import com.lz.user.model.dto.statistics.UserInformTypeStatisticsRo;
import com.lz.user.model.dto.statistics.UserStatisticsRequest;
import com.lz.user.service.IUStatisticsInfoService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 用户统计测试
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-10  16:56
 * @Version: 1.0
 */
@SpringBootTest
public class UStatisticsTest {
    @Resource
    private UStatisticsInfoMapper uStatisticsInfoMapper;

    @Resource
    private IUStatisticsInfoService uStatisticsInfoService;

    @Test
    public void testUserRegisterStatistics() {
        UserStatisticsRequest requestToday = new UserStatisticsRequest();
        requestToday.setStartDate(DateUtils.getDate());
        requestToday.setEndDate(DateUtils.getDate());
        requestToday.setIsDelete("0");
        List<StatisticsRo> statisticsRos = uStatisticsInfoMapper.userRegisterStatistics(requestToday);
        System.out.println("statisticsRos = " + statisticsRos);
        LineStatisticsVo lineStatisticsVo = uStatisticsInfoService.userRegisterStatistics(requestToday);
        System.out.println("lineStatisticsVo = " + lineStatisticsVo);
        requestToday.setStartDate("2025-09-09");
        requestToday.setEndDate("2025-09-09");
        statisticsRos = uStatisticsInfoMapper.userRegisterStatistics(requestToday);
        System.out.println("statisticsRos = " + statisticsRos);
        requestToday.setStartDate("2025-09-01");
        requestToday.setEndDate("2025-09-10");
        statisticsRos = uStatisticsInfoMapper.userRegisterStatistics(requestToday);
        System.out.println("statisticsRos = " + statisticsRos);
        lineStatisticsVo = uStatisticsInfoService.userRegisterStatistics(requestToday);
        System.out.println("lineStatisticsVo = " + lineStatisticsVo);
        requestToday.setStartDate("2025-07-30");
        requestToday.setEndDate("2025-09-10");
        statisticsRos = uStatisticsInfoMapper.userRegisterStatistics(requestToday);
        System.out.println("statisticsRos = " + statisticsRos);
        lineStatisticsVo = uStatisticsInfoService.userRegisterStatistics(requestToday);
        System.out.println("lineStatisticsVo = " + lineStatisticsVo);
    }

    @Test
    public void testUserSexStatistics() {
        RadarStatisticsVo pieStatisticsVo = uStatisticsInfoService.userAgeStatistics();
        System.out.println("pieStatisticsVo = " + pieStatisticsVo);
    }

    @Test
    public void testUserLocationStatistics() {
        List<MapStatisticsVo> mapStatisticsVos = uStatisticsInfoService.userLocationStatistics(null);
        mapStatisticsVos.forEach(System.out::println);
    }

    @Test
    public void testUserInformStatistics() {
        UserStatisticsRequest request = new UserStatisticsRequest();
        request.setStartDate("2025-09-01");
        request.setEndDate("2025-09-10");
        request.setIsDelete("0");
        List<UserInformTypeStatisticsRo> userInformTypeStatisticsRos = uStatisticsInfoMapper.userInformTypeStatistics(request);
        System.out.println("statisticsRos = " + userInformTypeStatisticsRos);
    }
}
