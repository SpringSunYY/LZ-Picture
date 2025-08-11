package com.lz.picture.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.CustomCacheable;
import com.lz.common.config.OssConfig;
import com.lz.common.config.RuoYiConfig;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.manager.file.PictureDownloadManager;
import com.lz.common.manager.file.model.BatchDownloadFileDto;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.common.utils.file.FileUtils;
import com.lz.picture.mapper.StatisticsInfoMapper;
import com.lz.picture.model.domain.StatisticsInfo;
import com.lz.picture.model.dto.pictureInfo.PictureInfoHotRequest;
import com.lz.picture.model.dto.statisticsInfo.StatisticsFileDto;
import com.lz.picture.model.dto.statisticsInfo.StatisticsInfoQuery;
import com.lz.picture.model.dto.statisticsInfo.StatisticsInfoRequest;
import com.lz.picture.model.vo.pictureInfo.PictureInfoStatisticsVo;
import com.lz.picture.model.vo.statisticsInfo.StatisticsInfoVo;
import com.lz.picture.service.IStatisticsInfoService;
import com.lz.picture.utils.MarkdownBuilder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lz.common.constant.redis.PictureRedisConstants.PICTURE_STATISTICS_STAGES;
import static com.lz.common.constant.redis.PictureRedisConstants.PICTURE_STATISTICS_STAGES_EXPIRE_TIME;
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
    @Resource
    private PictureDownloadManager pictureDownloadManager;
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
    public StatisticsInfo selectNewStatisticsInfoByCommonKeyAndType(String key, String type) {
        return this.getOne(new LambdaQueryWrapper<StatisticsInfo>().eq(StatisticsInfo::getCommonKey, key)
                .eq(StatisticsInfo::getType, type)
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
                    vo.setThumbnailUrl(OssConfig.builderPictureUrl(vo.getThumbnailUrl(), PICTURE_INDEX_P_VALUE));
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

    @CustomCacheable(keyPrefix = PICTURE_STATISTICS_STAGES, useQueryParamsAsKey = true, expireTime = PICTURE_STATISTICS_STAGES_EXPIRE_TIME)
    @Override
    public Long getStatisticsInfoStages(StatisticsInfoRequest request) {
        StatisticsInfo statisticsInfo = this.getOne(new LambdaQueryWrapper<StatisticsInfo>().eq(StatisticsInfo::getCommonKey, request.getCommonKey())
                .eq(StringUtils.isNotEmpty(request.getType()), StatisticsInfo::getType, request.getType())
                .orderBy(true, false, StatisticsInfo::getCreateTime).last("limit 1"));
        if (StringUtils.isNull(statisticsInfo)) {
            return 0L;
        }
        return statisticsInfo.getStages();
    }

    //region 获取统计文件
    @Override
    public StatisticsFileDto getStatisticsPictureHotFilePath(String type, String commonKey, String statisticsKey, Long stage, int number) {
        ThrowUtils.throwIf(StringUtils.isEmpty(statisticsKey) && StringUtils.isNull(stage) && StringUtils.isEmpty(commonKey) && StringUtils.isEmpty(type),
                "参数错误");
        //如果有统计key，则优先使用
        if (StringUtils.isNotEmpty(statisticsKey)) {
            stage = null;
        }
        StatisticsInfo statisticsInfo = this.getOne(
                new LambdaQueryWrapper<StatisticsInfo>().eq(StatisticsInfo::getCommonKey, commonKey)
                        .eq(StringUtils.isNotEmpty(type), StatisticsInfo::getType, type)
                        .eq(StringUtils.isNotEmpty(statisticsKey), StatisticsInfo::getStatisticsKey, statisticsKey)
                        .eq(StringUtils.isNotNull(stage), StatisticsInfo::getStages, stage)
                        .orderBy(true, false, StatisticsInfo::getCreateTime).last("limit 1"));
        ThrowUtils.throwIf(StringUtils.isNull(statisticsInfo) || StringUtils.isEmpty(statisticsInfo.getContent()), "未找到统计信息");
        //转换为 List
        List<PictureInfoStatisticsVo> statisticsVoList = JSONObject.parseArray(statisticsInfo.getContent(), PictureInfoStatisticsVo.class);
        List<PictureInfoStatisticsVo> vos = statisticsVoList.subList(0, Math.min(number, statisticsVoList.size()));
        //文件夹路径,替换statisticsKey里的: 为/
        String keyPath = StringUtils.replace(statisticsInfo.getStatisticsKey(), ":", File.separator);
        String bastPath = RuoYiConfig.getPicturePath() + keyPath;
        String imagesPath = bastPath + File.separator + "images";
        downloadHotPictureImage(vos, imagesPath);
        String mdPath = downloadHotPictureMd(statisticsInfo, bastPath, vos);
        //给文件压缩成 zip
        String zipPath = bastPath + File.separator + statisticsInfo.getStatisticsName() + "_" + System.currentTimeMillis() + ".zip";
        String compressZip = FileUtils.compressZip(List.of(imagesPath, mdPath), zipPath);
        //压缩成功删除md文件
        FileUtils.deleteFile(mdPath);
        return new StatisticsFileDto(FileUtils.getName(compressZip), compressZip);
    }

    /**
     * 生成md文件
     *
     * @param bastPath 基础路径
     * @param vos      vos
     */
    private String downloadHotPictureMd(StatisticsInfo statisticsInfo, String bastPath, List<PictureInfoStatisticsVo> vos) {
        //判断是否存在describe.md,如果存在直接删除
        String describePath = bastPath + File.separator + "图片热门统计_" + statisticsInfo.getStatisticsName() + "_" + System.currentTimeMillis() + ".md";
        if (FileUtils.isFileExists(describePath)) {
            FileUtils.deleteFile(describePath);
        }
        //开始写入md文件
        MarkdownBuilder builder = new MarkdownBuilder();
        builder.addHeading(statisticsInfo.getStatisticsName(), 1);
        builder.addParagraph(statisticsInfo.getRemark());
        builder.addParagraph("本次统计KEY:" + statisticsInfo.getStatisticsKey());
        builder.addHeading("图片列表", 2);
        //图片列表
        ArrayList<String> headers = new ArrayList<>(List.of("排名", "图片编号", "图片名称", "图片分数"));
        ArrayList<List<String>> rows = new ArrayList<>();
        //图片列表
        List<StatisticsFileDto> imageList = new ArrayList<>();
        for (PictureInfoStatisticsVo vo : vos) {
            //获取文件名
            if (StringUtils.isEmpty(vo.getThumbnailUrl())) {
                continue;
            }
            String fileName = FileUtils.getName(vo.getThumbnailUrl());
            rows.add(List.of(
                    String.valueOf(rows.size() + 1),
                    vo.getPictureId(),
                    vo.getName(),
                    String.valueOf(vo.getScore())
            ));
            StatisticsFileDto statisticsFileDto = new StatisticsFileDto(vo.getName(), "./images/" + fileName);
            imageList.add(statisticsFileDto);
        }
        builder.addTable(headers, rows);
        //图片列表
        builder.addHeading("图片列表", 2);
        for (int i = 0; i < imageList.size(); i++) {
            StatisticsFileDto file = imageList.get(i);
            builder.addHeading("排名:" + (i + 1), 3);
            builder.addParagraph(file.getFileName());
            builder.addImage(file.getFileName(), file.getFilePath());
        }
        try {
            builder.writeToFile(describePath);
        } catch (IOException e) {
            log.error("写入md文件失败:{}", e.getMessage());
            throw new RuntimeException("写入md文件失败");
        }
        return describePath;
    }

    /**
     * 下载图片
     *
     * @param vos        vos
     * @param imagesPath 图片保存路径
     * @return
     */
    private void downloadHotPictureImage(List<PictureInfoStatisticsVo> vos, String imagesPath) {
        List<BatchDownloadFileDto> batchDownloadFileDtos = new ArrayList<>();
        for (PictureInfoStatisticsVo vo : vos) {
            //获取文件名
            if (StringUtils.isEmpty(vo.getThumbnailUrl())) {
                continue;
            }
            String fileName = FileUtils.getName(vo.getThumbnailUrl());
            String localPath = imagesPath + File.separator + fileName;
            if (FileUtils.isFileExists(localPath)) {
                //存在则跳过
                continue;
            }
            BatchDownloadFileDto batchDownloadFileDto = new BatchDownloadFileDto();
            batchDownloadFileDto.setLocalPath(localPath);
            batchDownloadFileDto.setOssFilePath(vo.getThumbnailUrl());
            batchDownloadFileDtos.add(batchDownloadFileDto);
        }
        if (!batchDownloadFileDtos.isEmpty()) {
            //下载文件
            pictureDownloadManager.downloadFile(batchDownloadFileDtos);
        }
    }
    //endregion

}
