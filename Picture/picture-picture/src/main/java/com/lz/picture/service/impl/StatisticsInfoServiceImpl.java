package com.lz.picture.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.config.OssConfig;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.picture.mapper.StatisticsInfoMapper;
import com.lz.picture.model.domain.StatisticsInfo;
import com.lz.picture.model.dto.pictureInfo.PictureInfoHotRequest;
import com.lz.picture.model.dto.statisticsInfo.StatisticsInfoQuery;
import com.lz.picture.model.vo.pictureInfo.PictureInfoStatisticsVo;
import com.lz.picture.model.vo.statisticsInfo.StatisticsInfoVo;
import com.lz.picture.service.IStatisticsInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lz.config.utils.ConfigInfoUtils.PICTURE_INDEX_P_VALUE;

/**
 * 统计信息Service业务层处理
 *
 * @author YY
 * @date 2025-07-17
 */
@Slf4j
@Service
public class StatisticsInfoServiceImpl extends ServiceImpl<StatisticsInfoMapper, StatisticsInfo> implements IStatisticsInfoService {
    @Resource
    private StatisticsInfoMapper statisticsInfoMapper;
    @Resource
    private RedisCache redisCache;
    //region mybatis代码

    /**
     * 查询统计信息
     *
     * @param statisticsId 统计信息主键
     * @return 统计信息
     */
    @Override
    public StatisticsInfo selectStatisticsInfoByStatisticsId(String statisticsId) {
        return statisticsInfoMapper.selectStatisticsInfoByStatisticsId(statisticsId);
    }

    /**
     * 查询统计信息列表
     *
     * @param statisticsInfo 统计信息
     * @return 统计信息
     */
    @Override
    public List<StatisticsInfo> selectStatisticsInfoList(StatisticsInfo statisticsInfo) {
        return statisticsInfoMapper.selectStatisticsInfoList(statisticsInfo);
    }

    /**
     * 新增统计信息
     *
     * @param statisticsInfo 统计信息
     * @return 结果
     */
    @Override
    public int insertStatisticsInfo(StatisticsInfo statisticsInfo) {
        statisticsInfo.setCreateTime(DateUtils.getNowDate());
        return statisticsInfoMapper.insertStatisticsInfo(statisticsInfo);
    }

    /**
     * 修改统计信息
     *
     * @param statisticsInfo 统计信息
     * @return 结果
     */
    @Override
    public int updateStatisticsInfo(StatisticsInfo statisticsInfo) {
        return statisticsInfoMapper.updateStatisticsInfo(statisticsInfo);
    }

    /**
     * 批量删除统计信息
     *
     * @param statisticsIds 需要删除的统计信息主键
     * @return 结果
     */
    @Override
    public int deleteStatisticsInfoByStatisticsIds(String[] statisticsIds) {
        return statisticsInfoMapper.deleteStatisticsInfoByStatisticsIds(statisticsIds);
    }

    /**
     * 删除统计信息信息
     *
     * @param statisticsId 统计信息主键
     * @return 结果
     */
    @Override
    public int deleteStatisticsInfoByStatisticsId(String statisticsId) {
        return statisticsInfoMapper.deleteStatisticsInfoByStatisticsId(statisticsId);
    }

    //endregion
    @Override
    public QueryWrapper<StatisticsInfo> getQueryWrapper(StatisticsInfoQuery statisticsInfoQuery) {
        QueryWrapper<StatisticsInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = statisticsInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String statisticsId = statisticsInfoQuery.getStatisticsId();
        queryWrapper.eq(StringUtils.isNotEmpty(statisticsId), "statistics_id", statisticsId);

        String type = statisticsInfoQuery.getType();
        queryWrapper.eq(StringUtils.isNotEmpty(type), "type", type);

        String statisticsName = statisticsInfoQuery.getStatisticsName();
        queryWrapper.like(StringUtils.isNotEmpty(statisticsName), "statistics_name", statisticsName);

        String commonKey = statisticsInfoQuery.getCommonKey();
        queryWrapper.like(StringUtils.isNotEmpty(commonKey), "common_key", commonKey);

        String statisticsKey = statisticsInfoQuery.getStatisticsKey();
        queryWrapper.eq(StringUtils.isNotEmpty(statisticsKey), "statistics_key", statisticsKey);

        Long stages = statisticsInfoQuery.getStages();
        queryWrapper.eq(StringUtils.isNotNull(stages), "stages", stages);


        Date createTime = statisticsInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<StatisticsInfoVo> convertVoList(List<StatisticsInfo> statisticsInfoList) {
        if (StringUtils.isEmpty(statisticsInfoList)) {
            return Collections.emptyList();
        }
        return statisticsInfoList.stream().map(StatisticsInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public StatisticsInfo selectStatisticsInfoByStatisticsKey(String key) {
        return this.getOne(new LambdaQueryWrapper<StatisticsInfo>().eq(StatisticsInfo::getStatisticsKey, key)
                .orderBy(true, false, StatisticsInfo::getCreateTime).last("limit 1"));
    }

    @Override
    public StatisticsInfo selectNewStatisticsInfoByCommonKey(String key) {
        return this.getOne(new LambdaQueryWrapper<StatisticsInfo>().eq(StatisticsInfo::getCommonKey, key)
                .orderBy(true, false, StatisticsInfo::getCreateTime).last("limit 1"));
    }

    @Override
    public TableDataInfo getStatisticsPictureInfo(String statisticKey, PictureInfoHotRequest request) {
        //构建查询条件
        int start = (request.getPageNum() - 1) * request.getPageSize();
        int end = start + request.getPageSize() - 1;
        //首先判断缓存是否存在
        if (redisCache.hasKey(statisticKey)) {
            Long size = redisCache.getCacheListSize(statisticKey);
            List<PictureInfoStatisticsVo> cacheList = redisCache.getCacheList(statisticKey, start, end);
            return new TableDataInfo(cacheList, Math.toIntExact(size));
        }
        //如果不存在则查询数据库
        StatisticsInfo statisticsInfo = this.selectStatisticsInfoByStatisticsKey(statisticKey);
        if (StringUtils.isNotNull(statisticsInfo) && StringUtils.isNotNull(statisticsInfo.getContent())) {
            try {
                //转换为List
                List<PictureInfoStatisticsVo> statisticsVoList = JSONObject.parseArray(statisticsInfo.getContent(), PictureInfoStatisticsVo.class);
                for (PictureInfoStatisticsVo vo : statisticsVoList) {
                    vo.setThumbnailUrl(OssConfig.builderUrl(vo.getThumbnailUrl(), vo.getDnsUrl()) + "?x-oss-process=image/resize,p_" + PICTURE_INDEX_P_VALUE);
                }
                //缓存key，返回值
                redisCache.deleteObject(statisticKey);
                //这里直接指定，因为十分钟更新一次统计缓存
                redisCache.setCacheListRightPushAll(statisticKey, statisticsVoList, 15 * 60, TimeUnit.SECONDS);
                List<PictureInfoStatisticsVo> resultList = statisticsVoList.subList(Math.min(start, statisticsVoList.size()), Math.min(end, statisticsVoList.size()));
                return new TableDataInfo(resultList, statisticsVoList.size());
            } catch (Exception e) {
                log.error("获取图片统计信息失败:{}", e.getMessage());
                return new TableDataInfo(new ArrayList<>(), 0);
            }
        }
        return new TableDataInfo(new ArrayList<>(), 0);
    }

}
