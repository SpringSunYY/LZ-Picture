package com.lz.user.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.CustomCacheable;
import com.lz.common.annotation.CustomSort;
import com.lz.common.core.domain.statistics.ro.StatisticsRo;
import com.lz.common.core.domain.statistics.vo.LineStatisticsVo;
import com.lz.common.core.domain.statistics.vo.PieStatisticsVo;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.user.mapper.UStatisticsInfoMapper;
import com.lz.user.model.domain.UStatisticsInfo;
import com.lz.user.model.dto.statistics.UserStatisticsRequest;
import com.lz.user.model.dto.uStatisticsInfo.UStatisticsInfoQuery;
import com.lz.user.model.enums.UStatisticsTypeEnum;
import com.lz.user.model.enums.UUserSexEnum;
import com.lz.user.model.vo.uStatisticsInfo.UStatisticsInfoVo;
import com.lz.user.service.IUStatisticsInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.common.constant.user.UserStatisticsConstants.*;

/**
 * 统计信息Service业务层处理
 *
 * @author YY
 * @date 2025-09-09
 */
@Service
public class UStatisticsInfoServiceImpl extends ServiceImpl<UStatisticsInfoMapper, UStatisticsInfo> implements IUStatisticsInfoService {
    @Resource
    private UStatisticsInfoMapper uStatisticsInfoMapper;

    //region mybatis代码

    /**
     * 查询统计信息
     *
     * @param statisticsId 统计信息主键
     * @return 统计信息
     */
    @Override
    public UStatisticsInfo selectUStatisticsInfoByStatisticsId(String statisticsId) {
        return uStatisticsInfoMapper.selectUStatisticsInfoByStatisticsId(statisticsId);
    }

    /**
     * 查询统计信息列表
     *
     * @param uStatisticsInfo 统计信息
     * @return 统计信息
     */
    @CustomSort(sortFields = {"statisticsName", "commonKey", "statisticsKey", "createTime"},
            sortMappingFields = {"statistics_name", "common_key", "statistics_key", "create_time"})
    @Override
    public List<UStatisticsInfo> selectUStatisticsInfoList(UStatisticsInfo uStatisticsInfo) {
        return uStatisticsInfoMapper.selectUStatisticsInfoList(uStatisticsInfo);
    }

    /**
     * 新增统计信息
     *
     * @param uStatisticsInfo 统计信息
     * @return 结果
     */
    @Override
    public int insertUStatisticsInfo(UStatisticsInfo uStatisticsInfo) {
        uStatisticsInfo.setCreateTime(DateUtils.getNowDate());
        return uStatisticsInfoMapper.insertUStatisticsInfo(uStatisticsInfo);
    }

    /**
     * 修改统计信息
     *
     * @param uStatisticsInfo 统计信息
     * @return 结果
     */
    @Override
    public int updateUStatisticsInfo(UStatisticsInfo uStatisticsInfo) {
        return uStatisticsInfoMapper.updateUStatisticsInfo(uStatisticsInfo);
    }

    /**
     * 批量删除统计信息
     *
     * @param statisticsIds 需要删除的统计信息主键
     * @return 结果
     */
    @Override
    public int deleteUStatisticsInfoByStatisticsIds(String[] statisticsIds) {
        return uStatisticsInfoMapper.deleteUStatisticsInfoByStatisticsIds(statisticsIds);
    }

    /**
     * 删除统计信息信息
     *
     * @param statisticsId 统计信息主键
     * @return 结果
     */
    @Override
    public int deleteUStatisticsInfoByStatisticsId(String statisticsId) {
        return uStatisticsInfoMapper.deleteUStatisticsInfoByStatisticsId(statisticsId);
    }

    //endregion
    @Override
    public QueryWrapper<UStatisticsInfo> getQueryWrapper(UStatisticsInfoQuery uStatisticsInfoQuery) {
        QueryWrapper<UStatisticsInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = uStatisticsInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String statisticsId = uStatisticsInfoQuery.getStatisticsId();
        queryWrapper.eq(StringUtils.isNotEmpty(statisticsId), "statistics_id", statisticsId);

        String type = uStatisticsInfoQuery.getType();
        queryWrapper.eq(StringUtils.isNotEmpty(type), "type", type);

        String statisticsName = uStatisticsInfoQuery.getStatisticsName();
        queryWrapper.like(StringUtils.isNotEmpty(statisticsName), "statistics_name", statisticsName);

        String commonKey = uStatisticsInfoQuery.getCommonKey();
        queryWrapper.like(StringUtils.isNotEmpty(commonKey), "common_key", commonKey);

        String statisticsKey = uStatisticsInfoQuery.getStatisticsKey();
        queryWrapper.like(StringUtils.isNotEmpty(statisticsKey), "statistics_key", statisticsKey);

        Long stages = uStatisticsInfoQuery.getStages();
        queryWrapper.eq(StringUtils.isNotNull(stages), "stages", stages);

        Date createTime = uStatisticsInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<UStatisticsInfoVo> convertVoList(List<UStatisticsInfo> uStatisticsInfoList) {
        if (StringUtils.isEmpty(uStatisticsInfoList)) {
            return Collections.emptyList();
        }
        return uStatisticsInfoList.stream().map(UStatisticsInfoVo::objToVo).collect(Collectors.toList());
    }

    //region 用户统计
    @CustomCacheable(keyPrefix = USER_STATISTICS_REGISTER_DAY, expireTime = USER_STATISTICS_REGISTER_DAY_EXPIRE_TIME, useQueryParamsAsKey = true)
    @Override
    public LineStatisticsVo userRegisterStatistics(UserStatisticsRequest request) {
        //拿到开始结束时间
        String startDate = request.getStartDate();
        String endDate = request.getEndDate();
        Date nowDate = checkDate(startDate, endDate);
        List<String> dateRanges = DateUtils.getDateRanges(startDate, endDate);
        //如果为空查询全部
        if (StringUtils.isEmpty(dateRanges) || dateRanges == null) {
            return new LineStatisticsVo();
        }
        //灵活判断最近天数
        String end = dateRanges.getLast();
        LineStatisticsVo lineStatisticsVo = new LineStatisticsVo();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Long> totals = new ArrayList<>();
        //如果包含的日期有今天，则查询今天
        //是否包含今天
        boolean containsToday = dateRanges.contains(DateUtils.dateTime(nowDate));
        if (containsToday) {
            getUserRegisterStatistics(startDate, endDate, names, totals, lineStatisticsVo);
            //如果包含了且范围只有1，就表示统计今天
            if (dateRanges.size() == 1) {
                return lineStatisticsVo;
            }
            //删除今天,添加倒数第二天为最后一天
            dateRanges.removeLast();
            end = dateRanges.getLast();
        }
        //首先查询开始时间和结束时间-1这个时间范围内是否有数据，因为当天数据是会更新的，所以要新的查询
        List<UStatisticsInfo> uStatisticsInfoList = this.list(new LambdaQueryWrapper<UStatisticsInfo>()
                .eq(UStatisticsInfo::getType, UStatisticsTypeEnum.STATISTICS_TYPE_1.getValue())
                .eq(UStatisticsInfo::getCommonKey, USER_STATISTICS_REGISTER_DAY)
                .apply("date_format(create_time,'%Y-%m-%d') between {0} and {1}", startDate, end)
        );
        List<String> noStatisticsDate = new ArrayList<>(dateRanges);
        if (StringUtils.isNotEmpty(uStatisticsInfoList)) {
            //添加所有统计到的数据
            for (UStatisticsInfo uStatisticsInfo : uStatisticsInfoList) {
                String dateToStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, uStatisticsInfo.getCreateTime());
                names.add(dateToStr);
                String statisticsStr = uStatisticsInfo.getContent();
                StatisticsRo statisticsRo = JSONObject.parseObject(statisticsStr, StatisticsRo.class);
                totals.add(statisticsRo.getTotal());
                //如果没有统计，则添加
                noStatisticsDate.remove(dateToStr);
            }
        }
        if (StringUtils.isNotEmpty(noStatisticsDate)) {
            ArrayList<UStatisticsInfo> uStatisticsInfos = new ArrayList<>();
            for (String date : noStatisticsDate) {
                StatisticsRo userRegisterStatistics = getUserRegisterStatistics(date, date, names, totals, lineStatisticsVo);
                //统计数据
                UStatisticsInfo uStatisticsInfo = getUStatisticsInfo(date, userRegisterStatistics, UStatisticsTypeEnum.STATISTICS_TYPE_1.getValue(), USER_STATISTICS_REGISTER_DAY_NAME, USER_STATISTICS_REGISTER_DAY, 1L);
                uStatisticsInfos.add(uStatisticsInfo);
            }
            uStatisticsInfoMapper.insertOrUpdate(uStatisticsInfos);
        }
        //排序，根据时间排序
        lineStatisticsVo.getNames().sort(Comparator.naturalOrder());
        return lineStatisticsVo;
    }

    private static UStatisticsInfo getUStatisticsInfo(String date, Object content,
                                                      String type, String statisticsName,
                                                      String commonKey, Long stages) {
        UStatisticsInfo uStatisticsInfo = new UStatisticsInfo();
        uStatisticsInfo.setStatisticsId(IdUtils.snowflakeId().toString());
        uStatisticsInfo.setType(type);
        uStatisticsInfo.setStatisticsName(statisticsName + COMMON_SEPARATOR_CACHE + date);
        uStatisticsInfo.setCommonKey(commonKey);
        uStatisticsInfo.setStatisticsKey(commonKey + COMMON_SEPARATOR_CACHE + date);
        uStatisticsInfo.setStages(stages);
        uStatisticsInfo.setContent(JSONObject.toJSONString(content));
        uStatisticsInfo.setCreateTime(DateUtils.dateTime(DateUtils.YYYY_MM_DD, date));
        return uStatisticsInfo;
    }

    /**
     * 校验时间
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     */
    private static Date checkDate(String startDate, String endDate) {
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

    /**
     * 获取用户注册数据
     *
     * @param start            开始时间
     * @param end              结束时间
     * @param names            名称
     * @param totals           总数
     * @param lineStatisticsVo 折线图数据
     * @return
     */
    private StatisticsRo getUserRegisterStatistics(String start, String end, ArrayList<String> names, ArrayList<Long> totals, LineStatisticsVo lineStatisticsVo) {
        UserStatisticsRequest requestToday = new UserStatisticsRequest();
        requestToday.setStartDate(start);
        requestToday.setEndDate(end);
        List<StatisticsRo> userRegisterToday = uStatisticsInfoMapper.userRegisterStatistics(requestToday);
        StatisticsRo last = userRegisterToday.getLast();
        names.add(last.getName());
        totals.add(last.getTotal());
        lineStatisticsVo.setNames(names);
        lineStatisticsVo.setTotals(totals);
        return last;
    }

    @Override
    @CustomCacheable(keyPrefix = USER_STATISTICS_SEX, expireTime = USER_STATISTICS_SEX_EXPIRE_TIME)
    public PieStatisticsVo userSexStatistics() {
        PieStatisticsVo pieStatisticsVo = new PieStatisticsVo();
        ArrayList<PieStatisticsVo.Data> datas = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();

        //统计默认今天，表示最新
        String nowData = DateUtils.dateTime(DateUtils.getNowDate());
        //首先查询是否有存在
        UStatisticsInfo uStatisticsInfo = this.getOne(new LambdaQueryWrapper<UStatisticsInfo>()
                .eq(UStatisticsInfo::getType, UStatisticsTypeEnum.STATISTICS_TYPE_5.getValue())
                .eq(UStatisticsInfo::getCommonKey, USER_STATISTICS_SEX)
                .orderByDesc(UStatisticsInfo::getCreateTime)
                .last("limit 1"));
        //如果有数据且就是今天的
        if (StringUtils.isNotNull(uStatisticsInfo) && DateUtils.dateTime(uStatisticsInfo.getCreateTime()).equals(nowData)) {
            return JSONObject.parseObject(uStatisticsInfo.getContent(), PieStatisticsVo.class);
        }
        //没有数据，查询数据库
        List<StatisticsRo> userSexStatistics = uStatisticsInfoMapper.userSexStatistics(CommonDeleteEnum.NORMAL.getValue());
        for (StatisticsRo userSexStatistic : userSexStatistics) {
            Optional<UUserSexEnum> enumByValue = UUserSexEnum.getEnumByValue(userSexStatistic.getName());
            String name = enumByValue.isPresent() ? enumByValue.get().getLabel() : userSexStatistic.getName();
            PieStatisticsVo.Data data = new PieStatisticsVo.Data();
            data.setName(name);
            data.setValue(userSexStatistic.getTotal());
            datas.add(data);
            names.add(name);
        }
        pieStatisticsVo.setNames(names);
        pieStatisticsVo.setDatas(datas);
        UStatisticsInfo newUStatisticsInfo = getUStatisticsInfo(nowData, pieStatisticsVo,
                UStatisticsTypeEnum.STATISTICS_TYPE_5.getValue(), USER_STATISTICS_SEX_NAME, USER_STATISTICS_SEX,
                StringUtils.isNull(uStatisticsInfo) ? 1L : uStatisticsInfo.getStages() + 1);
        uStatisticsInfoMapper.insertOrUpdate(newUStatisticsInfo);
        return pieStatisticsVo;
    }
    //endregion

}
