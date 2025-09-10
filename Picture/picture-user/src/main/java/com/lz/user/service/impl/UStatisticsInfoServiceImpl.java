package com.lz.user.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.CustomSort;
import com.lz.common.core.domain.statistics.ro.StatisticsRo;
import com.lz.common.core.domain.statistics.vo.LineStatisticsVo;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.user.mapper.UStatisticsInfoMapper;
import com.lz.user.model.domain.UStatisticsInfo;
import com.lz.user.model.dto.statistics.UserStatisticsRequest;
import com.lz.user.model.dto.uStatisticsInfo.UStatisticsInfoQuery;
import com.lz.user.model.enums.UStatisticsTypeEnum;
import com.lz.user.model.vo.uStatisticsInfo.UStatisticsInfoVo;
import com.lz.user.service.IUStatisticsInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.common.constant.user.UserStatisticsConstants.USER_STATISTICS_REGISTER_DAY;
import static com.lz.common.constant.user.UserStatisticsConstants.USER_STATISTICS_REGISTER_DAY_NAME;

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
    @Override
    public LineStatisticsVo userRegisterStatistics(UserStatisticsRequest userStatisticsRequest) {
        //拿到开始结束时间
        String startDate = userStatisticsRequest.getStartDate();
        String endDate = userStatisticsRequest.getEndDate();
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
        Date nowDate = DateUtils.getNowDate();
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
        List<String> noStatisticsDate = new ArrayList<>();
        if (StringUtils.isNotEmpty(uStatisticsInfoList)) {
            //添加所有统计到的数据
            for (UStatisticsInfo uStatisticsInfo : uStatisticsInfoList) {
                String dateToStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, uStatisticsInfo.getCreateTime());
                names.add(dateToStr);
                String statisticsStr = uStatisticsInfo.getContent();
                StatisticsRo statisticsRo = JSONObject.parseObject(statisticsStr, StatisticsRo.class);
                totals.add(statisticsRo.getTotal());
                //去除统计key的公告前缀;
                //如果没有统计，则添加
                if (!dateRanges.contains(dateToStr)) {
                    noStatisticsDate.add(dateToStr);
                }
            }
        } else {
            noStatisticsDate.addAll(dateRanges);
        }
        if (StringUtils.isNotEmpty(noStatisticsDate)) {
            ArrayList<UStatisticsInfo> uStatisticsInfos = new ArrayList<>();
            for (String date : noStatisticsDate) {
                StatisticsRo userRegisterStatistics = getUserRegisterStatistics(date, date, names, totals, lineStatisticsVo);
                UStatisticsInfo uStatisticsInfo = new UStatisticsInfo();
                uStatisticsInfo.setStatisticsId(IdUtils.snowflakeId().toString());
                uStatisticsInfo.setType(UStatisticsTypeEnum.STATISTICS_TYPE_1.getValue());
                uStatisticsInfo.setStatisticsName(USER_STATISTICS_REGISTER_DAY_NAME + COMMON_SEPARATOR_CACHE + date);
                uStatisticsInfo.setCommonKey(USER_STATISTICS_REGISTER_DAY);
                uStatisticsInfo.setStatisticsKey(USER_STATISTICS_REGISTER_DAY + COMMON_SEPARATOR_CACHE + date);
                uStatisticsInfo.setStages(1L);
                uStatisticsInfo.setContent(JSONObject.toJSONString(userRegisterStatistics));
                uStatisticsInfo.setCreateTime(DateUtils.dateTime(DateUtils.YYYY_MM_DD, date));
                uStatisticsInfos.add(uStatisticsInfo);
            }
            uStatisticsInfoMapper.insertOrUpdate(uStatisticsInfos);
        }
        //排序，根据时间排序
        lineStatisticsVo.getNames().sort(Comparator.naturalOrder());
        return lineStatisticsVo;
    }

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
    //endregion

}
