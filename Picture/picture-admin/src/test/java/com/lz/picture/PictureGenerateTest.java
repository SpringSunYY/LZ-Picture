package com.lz.picture;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.enums.CommonHasStatisticsEnum;
import com.lz.common.utils.RandomUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.picture.model.domain.*;
import com.lz.picture.model.enums.*;
import com.lz.picture.service.*;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.service.IUserInfoService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.map.LinkedMap;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR;

/**
 * 图片生成测试类
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-21  15:32
 * @Version: 1.0
 */
@SpringBootTest
public class PictureGenerateTest {
    @Resource
    private ISearchLogInfoService searchLogInfoService;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private ISpaceInfoService spaceInfoService;

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private IPictureCategoryInfoService pictureCategoryInfoService;

    @Resource
    private IPictureInfoService pictureInfoService;

    @Resource
    private IPictureTagInfoService pictureTagInfoService;

    @Resource
    private IPictureTagRelInfoService pictureTagRelInfoService;

    @Resource
    private IPictureDownloadLogInfoService pictureDownloadLogInfoService;

    @Resource
    private IUserBehaviorInfoService userBehaviorInfoService;

    @Resource
    private IStatisticsInfoService statisticsInfoService;

    @Resource
    private IUserViewLogInfoService userViewLogInfoService;

    List<String> keywords = List.of(
            "YY", "Spring", "懒羊羊", "AI", "海绵宝宝", "蜡笔小新", "可爱壁纸", "搞笑表情包", "表情包", "可爱的壁纸", "哆啦A梦",
            "AI生图", "AI图片生成", "AI图片", "手机壁纸", "电脑高清壁纸", "壁纸", "AI氛围感", "懒羊羊头像", "懒羊羊表情包", "AI生成图片",
            "小黄鸭表情包", "小黄鸭头像", "城市夜景", "citywork", "广州", "可爱表情包", "手机壁纸", "搞笑壁纸", "Jay", "Jay Zhou", "Hello kitty",
            "wallhaven", "聊天背景", "室内特写", "脸部特写", "春节", "小黄鸭", "小心超人", "熊二", "好运连连", "心想事成", "恭喜发财",
            "卡通头像", "AI生成头像", "壁纸ins高级质感", "故事宿命感", "自然真实", "大师摄影", "真实人像摄影", "cosplay", "游戏cg", "夕阳", "日出",
            "蓝调", "猫咪", "Moebius", "头像插画", "极简主义", "复古风格", "艺术风格", "大师作品", "4K", "4k高清"
    );
    List<String> tags = List.of(
            "YY", "Spring", "懒羊羊", "AI", "海绵宝宝", "蜡笔小新", "可爱壁纸", "搞笑表情包", "表情包", "可爱的壁纸", "哆啦A梦",
            "AI生图", "AI图片生成", "AI图片", "手机壁纸", "电脑高清壁纸", "壁纸", "AI氛围感", "懒羊羊头像", "懒羊羊表情包", "AI生成图片",
            "小黄鸭表情包", "小黄鸭头像", "城市夜景", "citywork", "广州", "可爱表情包", "手机壁纸", "搞笑壁纸", "Jay", "Jay Zhou", "Hello kitty",
            "wallhaven", "聊天背景", "室内特写", "脸部特写", "春节", "小黄鸭", "小心超人", "熊二", "好运连连", "心想事成", "恭喜发财",
            "卡通头像", "AI生成头像", "壁纸ins高级质感", "故事宿命感", "自然真实", "大师摄影", "真实人像摄影", "cosplay", "游戏cg", "夕阳", "日出",
            "蓝调", "猫咪", "Moebius", "头像插画", "极简主义", "复古风格", "艺术风格", "大师作品", "4K", "4k高清", "熊出没", "头像", "即梦AI", "动漫",
            "火影忍者", "学校", "公园", "街景", "图书", "文学", "生活", "电影", "游戏", "音乐", "书籍", "小说", "漫画", " manga", "book", "novel", "comic", "music",
            "movie", "game", "book", "novel", "comic", "music", "movie", "game", "book", "novel", "winter", "summer", "spring", "autumn", "winter", "street", "city", "park", "street"
    );

    /**
     * 生成搜索记录
     */
    @Test
    public void testGenerateSearch() {
        LinkedMap<Integer, List<SearchLogInfo>> generateMap = new LinkedMap<>();
        int insertCount = 0;
        List<UserInfo> userInfoList = userInfoService.list(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                .orderByDesc(UserInfo::getCreateTime)
                .last("limit 10000"));
        System.out.println("keywords = " + keywords.size());
        for (int i = 0; i < keywords.size(); i++) {
            ArrayList<SearchLogInfo> searchLogInfos = new ArrayList<>();
            for (UserInfo userInfo : userInfoList) {
                insertCount++;
                SearchLogInfo searchLogInfo = new SearchLogInfo();
                searchLogInfo.setSearchId(IdUtils.snowflakeId().toString());
                searchLogInfo.setUserId(userInfo.getUserId());
                Random random = new Random();
                int i1 = random.nextInt(i + 1);
                searchLogInfo.setKeyword(keywords.get(i1));
                searchLogInfo.setSearchType(PSearchTypeEnum.SEARCH_TYPE_0.getValue());
                searchLogInfo.setReferSource(PSearchReferSourceEnum.SEARCH_REFER_SOURCE_0.getValue());
                searchLogInfo.setSearchStatus(PSearchStatusEnum.SEARCH_STATUS_0.getValue());
                searchLogInfo.setFailReason(null);
                searchLogInfo.setResultCount(35L);
                searchLogInfo.setCreateTime(RandomUtils.generateDate(2025, 2025));
                searchLogInfo.setSearchDuration(1000L);
                searchLogInfo.setIpAddr(RandomUtils.generateRandomIpAddr());
                searchLogInfo.setIpAddress(RandomUtils.generateRandomIPAddress());
                searchLogInfo.setBrowser("Chrome");
                searchLogInfo.setOs("Windows 10");
                searchLogInfo.setPlatform("Windows");
                searchLogInfo.setDeviceId(null);
                searchLogInfos.add(searchLogInfo);
            }
            generateMap.put(i, searchLogInfos);
        }
        System.out.println("insertCount = " + insertCount);

        List<Future<Boolean>> futures = new ArrayList<>();
        for (List<SearchLogInfo> searchLogInfos : generateMap.values()) {
            Future<Boolean> future = threadPoolTaskExecutor.submit(() -> {
                return searchLogInfoService.saveBatch(searchLogInfos);
            });
            futures.add(future);
        }
        futures.forEach(future -> {
            try {
                Boolean result = future.get();
                System.out.println("result = " + result);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 测试生成空间
     */
    @Test
    public void testGenerateSpace() {
        LinkedMap<Integer, List<SpaceInfo>> generateMap = new LinkedMap<>();
        int insertCount = 0;
        String spaceName = "Space-";
        String spaceType1 = "1";
        String spaceType2 = "2";
        List<UserInfo> userInfoList = userInfoService.list(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                .orderByDesc(UserInfo::getCreateTime)
                .last("limit 10000"));
        for (int i = 0; i < 10; i++) {
            ArrayList<SpaceInfo> spaceInfos = new ArrayList<>();
            for (UserInfo userInfo : userInfoList) {
                insertCount++;
                SpaceInfo spaceInfo = new SpaceInfo();
                spaceInfo.setSpaceId(IdUtils.snowflakeId().toString());
                boolean b = i % 2 == 0;
                spaceInfo.setSpaceName(b ? spaceName + "团队" + i : spaceName + "个人" + i);
                spaceInfo.setSpaceAvatar("/picture/cover/2025/07/30/TTYY-1950539728734523393-compressed.webp");
                spaceInfo.setMaxSize(1073741824L);
                spaceInfo.setMaxCount(300L);
                spaceInfo.setLookCount(0L);
                spaceInfo.setCollectCount(0L);
                spaceInfo.setDownloadCount(0L);
                spaceInfo.setTotalSize(0L);
                spaceInfo.setTotalCount(0L);
                spaceInfo.setUserId(userInfo.getUserId());
                spaceInfo.setSpaceDesc("");
                spaceInfo.setSpaceStatus(PSpaceStatusEnum.SPACE_STATUS_1.getValue());
                spaceInfo.setSpaceType(b ? spaceType1 : spaceType2);
                spaceInfo.setMemberLimit(b ? 10L : 1L);
                spaceInfo.setCurrentMembers(1L);
                spaceInfo.setCreateTime(RandomUtils.generateDate(2025, 2025));
                spaceInfo.setLastUpdateTime(null);
                spaceInfo.setUpdateTime(null);
                spaceInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
                spaceInfo.setDeletedTime(null);
                spaceInfos.add(spaceInfo);
            }
            generateMap.put(i, spaceInfos);
        }
        System.out.println("insertCount = " + insertCount);
        for (List<SpaceInfo> spaceInfos : generateMap.values()) {
            spaceInfoService.saveBatch(spaceInfos);
        }
    }

    /**
     * 测试生成图片
     */
    @Test
    public void testGeneratePicture() {
        String thumbnailUrl = "/picture/picture/2025/07/31/50b936c6136898cf57689ab4be404ea1-1950595431075549185-compressed.webp";
        String pictureUrl = "/picture/picture/2025/07/31/50b936c6136898cf57689ab4be404ea1-1950595431075549184.jpg";
        //查询最新的10000个空间 团队空间
//        List<SpaceInfo> spaceInfoList = spaceInfoService.list(new LambdaQueryWrapper<SpaceInfo>()
//                .notIn(SpaceInfo::getSpaceId, List.of("1950538503299981313", "1950538697844383745", "1950539775252029441", "1950582726715973634"))
//                .eq(SpaceInfo::getSpaceType, PSpaceTypeEnum.SPACE_TYPE_1.getValue())
//                .last("limit 6000"));
        //官方空间
//        List<SpaceInfo> spaceInfoList = spaceInfoService.list(new LambdaQueryWrapper<SpaceInfo>()
//                .eq(SpaceInfo::getSpaceType, PSpaceTypeEnum.SPACE_TYPE_0.getValue())
//                .eq(SpaceInfo::getSpaceId, "2011111829029982210")
//                .last("limit 6000"));
        //个人空间
        List<SpaceInfo> spaceInfoList = spaceInfoService.list(new LambdaQueryWrapper<SpaceInfo>()
                .notIn(SpaceInfo::getSpaceId, List.of("1950538503299981313", "1950538697844383745", "1950539775252029441", "1950582726715973634"))
                .eq(SpaceInfo::getSpaceType, PSpaceTypeEnum.SPACE_TYPE_2.getValue())
                .last("limit 6000"));
        //查询到不是顶级的分类
        List<PictureCategoryInfo> categoryInfoList = pictureCategoryInfoService.list(new LambdaQueryWrapper<PictureCategoryInfo>()
                .ne(PictureCategoryInfo::getCategoryId, "0")
        );
        for (String tag : tags) {
            PictureTagInfo one = pictureTagInfoService.getOne(new LambdaQueryWrapper<PictureTagInfo>().eq(PictureTagInfo::getName, tag));
            if (StringUtils.isNotNull(one)) {
                continue;
            }
            PictureTagInfo pictureTagInfo = new PictureTagInfo();
            pictureTagInfo.setTagId(IdUtils.snowflakeId().toString());
            pictureTagInfo.setName(tag);
            pictureTagInfo.setTagsStatus(PTagStatusEnum.TAG_STATUS_0.getValue());
            pictureTagInfo.setTagDesc("");
            pictureTagInfo.setUsageCount(0L);
            pictureTagInfo.setLookCount(0L);
            pictureTagInfo.setDownloadCount(0L);
            pictureTagInfo.setUserId("1967144087123529728");
            Date createTime = RandomUtils.generateDate(2025, 2026);
            pictureTagInfo.setCreateTime(createTime);
            pictureTagInfo.setUpdateTime(createTime);
            pictureTagInfoService.save(pictureTagInfo);
        }
        //查询当前已经拥有的标签
        List<PictureTagInfo> pictureTagInfoList = pictureTagInfoService.list();

        List<PictureInfo> pictureInfos = new ArrayList<>();
        ArrayList<PictureTagRelInfo> pictureTagRelInfos = new ArrayList<>();
        int min = 1024 * 1024;
        int max = 1024 * 1024 * 10;
        int count = 0;
        int spaceCount = 5000;
        Random random = new Random();
        for (SpaceInfo spaceInfo : spaceInfoList) {
            String spaceId = spaceInfo.getSpaceId();
            String userId = spaceInfo.getUserId();
            long l = Long.parseLong(spaceId) % 3 + 1;
//            long l = Long.parseLong(spaceId) % 2 + 1;
//            long l = 1;
            String type = String.valueOf(l);
            for (int i = 0; i < spaceCount; i++) {
                for (PictureCategoryInfo pictureCategoryInfo : categoryInfoList) {
                    PictureInfo pictureInfo = new PictureInfo();
                    pictureInfo.setPictureId(IdUtils.snowflakeId().toString());
                    pictureInfo.setPictureUrl(pictureUrl);
                    pictureInfo.setName(pictureCategoryInfo.getName() + spaceInfo.getSpaceName());
                    pictureInfo.setIntroduction("");
                    pictureInfo.setCategoryId(pictureCategoryInfo.getCategoryId());
                    long picSize = random.nextLong(min, max);
                    pictureInfo.setPicSize(picSize);
                    pictureInfo.setPicWidth(1259);
                    pictureInfo.setPicHeight(1259);
                    pictureInfo.setPicScale(1.0);
                    pictureInfo.setPicFormat("jpg");
                    pictureInfo.setUserId(userId);
                    pictureInfo.setCreateTime(RandomUtils.generateDate(2025, 2026));
                    pictureInfo.setPictureStatus(PPictureStatusEnum.PICTURE_STATUS_0.getValue());
                    pictureInfo.setThumbnailUrl(thumbnailUrl);
                    pictureInfo.setLookCount(0L);
                    pictureInfo.setCollectCount(0L);
                    pictureInfo.setLikeCount(0L);
                    pictureInfo.setShareCount(0L);
                    pictureInfo.setDownloadCount(0L);
                    pictureInfo.setSpaceId(spaceId);
                    pictureInfo.setUploadType(type);
                    pictureInfo.setIsDelete("1");
                    pictureInfos.add(pictureInfo);
                    //空间
                    spaceInfo.setTotalSize(spaceInfo.getTotalSize() + picSize);
                    spaceInfo.setTotalCount(spaceInfo.getTotalCount() + 1);
                    //分类
                    pictureCategoryInfo.setUsageCount(pictureCategoryInfo.getUsageCount() + 1);

                    //标签
                    //随机获取5个标签
                    int i3 = random.nextInt(tags.size() - 5);
                    int i2 = random.nextInt(5);
                    for (int j = i; j < i3 + i2; j++) {
                        PictureTagInfo tag = pictureTagInfoList.get(i);
                        tag.setUsageCount(tag.getUsageCount() + 1);
                        PictureTagRelInfo tagRelInfo = new PictureTagRelInfo();
                        tagRelInfo.setPictureId(pictureInfo.getPictureId());
                        tagRelInfo.setTagId(tag.getTagId());
                        tagRelInfo.setTagName(tag.getName());
                        tagRelInfo.setPictureName(pictureInfo.getName());
                        tagRelInfo.setLookCount(0L);
                        tagRelInfo.setCollectCount(0L);
                        tagRelInfo.setLikeCount(0L);
                        tagRelInfo.setShareCount(0L);
                        tagRelInfo.setDownloadCount(0L);
                        tagRelInfo.setUserId(userId);
                        tagRelInfo.setCreateTime(pictureInfo.getCreateTime());
                        pictureTagRelInfos.add(tagRelInfo);
                    }

                    count++;
                }
            }

        }
        System.out.println("count = " + count);
        spaceInfoService.updateBatchById(spaceInfoList);
        pictureCategoryInfoService.updateBatchById(categoryInfoList);
        pictureTagInfoService.updateBatchById(pictureTagInfoList);
        pictureInfoService.saveBatch(pictureInfos);
        pictureTagRelInfoService.saveBatch(pictureTagRelInfos);
    }

    @Test
    public void testGenerateDownloadPicture() {
        List<UserInfo> userInfoList = userInfoService.list(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                .orderByDesc(UserInfo::getCreateTime)
                .last("limit 10000"));
        List<PictureInfo> pictureInfos = pictureInfoService.list(new LambdaQueryWrapper<PictureInfo>()
                .last("limit 100")
                .notIn(PictureInfo::getSpaceId, List.of("1950538503299981313", "1950538697844383745", "1950539775252029441", "1950582726715973634"))
        );
        List<String> pictureIds = pictureInfos.stream().map(PictureInfo::getPictureId).toList();
        List<PictureTagRelInfo> tagRelInfos = pictureTagRelInfoService.list(new LambdaQueryWrapper<PictureTagRelInfo>().select(PictureTagRelInfo::getPictureId, PictureTagRelInfo::getTagName)
                .in(PictureTagRelInfo::getPictureId, pictureIds));
        Map<String, List<String>> tagMap = tagRelInfos.stream()
                .collect(Collectors.groupingBy(PictureTagRelInfo::getPictureId, Collectors.mapping(PictureTagRelInfo::getTagName, Collectors.toList())));
        HashMap<String, List<PictureDownloadLogInfo>> insertMap = new HashMap<>();
        int count = 0;
        for (UserInfo userInfo : userInfoList) {
            String userId = userInfo.getUserId();
            List<PictureDownloadLogInfo> list = new ArrayList<>();
            for (PictureInfo pictureInfo : pictureInfos) {
                PictureDownloadLogInfo pictureDownloadLogInfo = new PictureDownloadLogInfo();
                pictureDownloadLogInfo.setDownloadId(IdUtils.snowflakeId().toString());
                pictureDownloadLogInfo.setUserId(userId);
                pictureDownloadLogInfo.setPictureId(pictureInfo.getPictureId());
                pictureDownloadLogInfo.setCategoryId(pictureInfo.getCategoryId());
                pictureDownloadLogInfo.setPictureName(pictureInfo.getName());
                pictureDownloadLogInfo.setThumbnailUrl(pictureInfo.getThumbnailUrl());
                List<String> elements = tagMap.get(pictureInfo.getPictureId());
                if (StringUtils.isNotEmpty(elements)) {
                    pictureDownloadLogInfo.setTags(String.join(COMMON_SEPARATOR, elements));
                }
                pictureDownloadLogInfo.setSpaceId(pictureInfo.getSpaceId());
                pictureDownloadLogInfo.setPointsCost(0L);
                pictureDownloadLogInfo.setPointsAuthorGain(0L);
                pictureDownloadLogInfo.setPointsOfficialGain(0L);
                pictureDownloadLogInfo.setPointsSpaceGain(0L);
                pictureDownloadLogInfo.setAuthorProportion(BigDecimal.valueOf(0.0));
                pictureDownloadLogInfo.setOfficialProportion(BigDecimal.valueOf(0.0));
                pictureDownloadLogInfo.setSpaceProportion(BigDecimal.valueOf(0.0));
                pictureDownloadLogInfo.setCreateTime(RandomUtils.generateDate(2025, 2026));
                pictureDownloadLogInfo.setDownloadStatus(PDownloadStatusEnum.DOWNLOAD_STATUS_0.getValue());
                pictureDownloadLogInfo.setFailReason(null);
                pictureDownloadLogInfo.setDownloadType(PDownloadTypeEnum.DOWNLOAD_TYPE_1.getValue());
                pictureDownloadLogInfo.setReferSource(PDownloadReferSourceEnum.DOWNLOAD_REFER_SOURCE_0.getValue());
                pictureDownloadLogInfo.setHasStatistics(CommonHasStatisticsEnum.HAS_STATISTICS_0.getValue());
                pictureDownloadLogInfo.setScore(20.0);
                pictureDownloadLogInfo.setIpAddr(RandomUtils.generateRandomIpAddr());
                pictureDownloadLogInfo.setIpAddress(RandomUtils.generateRandomIPAddress());
                pictureDownloadLogInfo.setDeviceId(null);
                pictureDownloadLogInfo.setBrowser("Chrome");
                pictureDownloadLogInfo.setOs("Windows 10");
                pictureDownloadLogInfo.setPlatform("Windows");

                list.add(pictureDownloadLogInfo);
                insertMap.put(userId, list);
                count++;
            }
            insertMap.put(userId, list);
        }
        System.out.println("count = " + count);
        insertMap.forEach((userId, list) -> {
            pictureDownloadLogInfoService.saveBatch(list);
        });
    }

    @Test
    public void testGenerateBehavior() {
        List<UserInfo> userInfoList = userInfoService.list(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                .orderByDesc(UserInfo::getCreateTime)
                .last("limit 1000"));
        List<PictureInfo> pictureInfos = pictureInfoService.list(new LambdaQueryWrapper<PictureInfo>()
                .notIn(PictureInfo::getSpaceId, List.of("1950538503299981313", "1950538697844383745", "1950539775252029441", "1950582726715973634"))
                .last("limit 100"));
        Map<String, List<UserBehaviorInfo>> insertMap = new HashMap<>();
        List<String> pictureIds = pictureInfos.stream().map(PictureInfo::getPictureId).toList();
        List<PictureTagRelInfo> tagRelInfos = pictureTagRelInfoService.list(new LambdaQueryWrapper<PictureTagRelInfo>()
                .select(PictureTagRelInfo::getPictureId, PictureTagRelInfo::getTagName)
                .in(PictureTagRelInfo::getPictureId, pictureIds));
        Map<String, List<String>> tagMap = tagRelInfos.stream()
                .collect(Collectors.groupingBy(PictureTagRelInfo::getPictureId, Collectors.mapping(PictureTagRelInfo::getTagName, Collectors.toList())));
        ArrayList<String> targetType = new ArrayList<>();
        PUserBehaviorTypeEnum[] values = PUserBehaviorTypeEnum.values();
        for (PUserBehaviorTypeEnum value : values) {
            targetType.add(value.getValue());
        }
        int count = 0;
        Random random = new Random();
        for (UserInfo userInfo : userInfoList) {
            String userId = userInfo.getUserId();
            List<UserBehaviorInfo> list = new ArrayList<>();
            for (PictureInfo pictureInfo : pictureInfos) {
                UserBehaviorInfo info = new UserBehaviorInfo();
                info.setBehaviorId(IdUtils.snowflakeId().toString());
                info.setBehaviorType(targetType.get(random.nextInt(0, targetType.size())));
                info.setUserId(userId);
                info.setTargetType(PUserBehaviorTargetTypeEnum.USER_BEHAVIOR_TARGET_TYPE_0.getValue());
                info.setTargetId(pictureInfo.getPictureId());
                info.setTargetContent(pictureInfo.getName());
                info.setScore(0.0);
                info.setShareLink("");
                info.setCategoryId(pictureInfo.getCategoryId());
                info.setSpaceId(pictureInfo.getSpaceId());
                List<String> elements = tagMap.get(pictureInfo.getPictureId());
                if (StringUtils.isNotEmpty(elements)) {
                    info.setTags(String.join(COMMON_SEPARATOR, elements));
                }
                info.setTargetCover(pictureInfo.getThumbnailUrl());
                info.setCreateTime(RandomUtils.generateDate(2025, 2025));
                info.setHasStatistics(CommonHasStatisticsEnum.HAS_STATISTICS_0.getValue());
                info.setDeviceId(null);
                info.setBrowser("Chrome");
                info.setOs("Windows 11");
                info.setPlatform("Windows");
                info.setIpAddr(RandomUtils.generateRandomIpAddr());
                info.setIpAddress(RandomUtils.generateRandomIPAddress());

                list.add(info);
                count++;
            }
            insertMap.put(userId, list);
        }
        System.out.println(count);
        insertMap.forEach((userId, list) -> {
            userBehaviorInfoService.saveBatch(list);
        });
    }

    /**
     * 删除统计内容
     */
    @Test
    public void deleteStatistics() {
        List<StatisticsInfo> list = statisticsInfoService.list(new LambdaQueryWrapper<StatisticsInfo>()
                .notIn(StatisticsInfo::getType, List.of(
                        PStatisticsTypeEnum.STATISTICS_TYPE_1.getValue(),
                        PStatisticsTypeEnum.STATISTICS_TYPE_2.getValue(),
                        PStatisticsTypeEnum.STATISTICS_TYPE_3.getValue(),
                        PStatisticsTypeEnum.STATISTICS_TYPE_4.getValue(),
                        PStatisticsTypeEnum.STATISTICS_TYPE_5.getValue(),
                        PStatisticsTypeEnum.STATISTICS_TYPE_6.getValue()
                )));
        List<String> ids = list.stream().map(StatisticsInfo::getStatisticsId).toList();
        statisticsInfoService.removeByIds(ids);
    }


    @Test
    public void deletePicture() {
        //根据id转为 map
        int pageSize = 10000;
        int currentPage = 1;
        boolean hasMoreData = true;
        LambdaQueryWrapper<SpaceInfo> queryWrapper = new LambdaQueryWrapper<SpaceInfo>()
                .notIn(SpaceInfo::getSpaceId, List.of("1950538503299981313", "1950538697844383745", "1950539775252029441", "1950582726715973634"));
        while (hasMoreData) {
            Page<SpaceInfo> page = new Page<>(currentPage, pageSize);

            // 执行分页查询
            Page<SpaceInfo> resultPage = spaceInfoService.page(page, queryWrapper);
            //遍历每一条数据查询到图片
            resultPage.getRecords().forEach(this::deletePicture);
            // 判断是否还有更多数据
            hasMoreData = currentPage < resultPage.getPages();
            currentPage++;
            System.out.println("当前页：" + currentPage);
        }
        spaceInfoService.remove(queryWrapper);

        //删除完成需删除1950538697844383745此空间图片
        SpaceInfo spaceInfo = spaceInfoService.getById("1950538697844383745");
        deletePicture(spaceInfo);
        spaceInfo.setTotalCount(0L);
        spaceInfo.setTotalSize(0L);
        spaceInfoService.updateById(spaceInfo);

        //查询到剩下的图片，为分类计算使用数
        List<PictureInfo> pictureInfos = pictureInfoService.list(new LambdaQueryWrapper<PictureInfo>());
        HashMap<String, Long> map = new HashMap<>();
        pictureInfos.forEach(pictureInfo -> {
            String categoryId = pictureInfo.getCategoryId();
            if (map.containsKey(categoryId)) {
                map.put(categoryId, map.get(categoryId) + 1);
            } else {
                map.put(categoryId, 1L);
            }
        });
        map.forEach((categoryId, count) -> {
            PictureCategoryInfo categoryInfo = pictureCategoryInfoService.getById(categoryId);
            if (StringUtils.isNotNull(categoryInfo)) {
                categoryInfo.setUsageCount(count);
                pictureCategoryInfoService.updateById(categoryInfo);
            }
        });
        //删除所有的搜索记录
        searchLogInfoService.remove(new LambdaQueryWrapper<SearchLogInfo>());
    }

    private void deletePicture(SpaceInfo spaceInfo) {
        List<PictureInfo> pictureInfos = pictureInfoService.list(new LambdaQueryWrapper<PictureInfo>()
                .eq(PictureInfo::getSpaceId, spaceInfo.getSpaceId()));
        //拿到所有的图片id
        List<String> pictureIds = new ArrayList<>();
        //先更新图片分类
        pictureInfos.forEach(pictureInfo -> {
            pictureIds.add(pictureInfo.getPictureId());
        });
        if (StringUtils.isEmpty(pictureIds)) {
            return;
        }
        //需删除用户行为、用户浏览记录、下载记录、标签关联
        userBehaviorInfoService.remove(new LambdaQueryWrapper<UserBehaviorInfo>().in(UserBehaviorInfo::getTargetId, pictureIds));
        userViewLogInfoService.remove(new LambdaQueryWrapper<UserViewLogInfo>().in(UserViewLogInfo::getTargetId, pictureIds));
        pictureDownloadLogInfoService.remove(new LambdaQueryWrapper<PictureDownloadLogInfo>().in(PictureDownloadLogInfo::getPictureId, pictureIds));
        pictureTagRelInfoService.remove(new LambdaQueryWrapper<PictureTagRelInfo>().in(PictureTagRelInfo::getPictureId, pictureIds));
        pictureInfoService.remove(new LambdaQueryWrapper<PictureInfo>().in(PictureInfo::getPictureId, pictureIds));
    }
}
