package com.lz.picture;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.enums.CommonHasStatisticsEnum;
import com.lz.common.utils.RandomUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.config.model.domain.InformTemplateInfo;
import com.lz.config.model.enmus.CTemplateTypeEnum;
import com.lz.config.service.IInformTemplateInfoService;
import com.lz.picture.model.domain.*;
import com.lz.picture.model.dto.pictureInfo.PictureMoreInfo;
import com.lz.picture.model.enums.*;
import com.lz.picture.service.*;
import com.lz.points.model.domain.AccountInfo;
import com.lz.points.model.domain.PointsUsageLogInfo;
import com.lz.points.model.enums.PoPointsUsageLogTypeEnum;
import com.lz.points.model.enums.PoPointsUsageTypeEnum;
import com.lz.points.service.IAccountInfoService;
import com.lz.points.service.IPointsUsageLogInfoService;
import com.lz.user.model.domain.InformInfo;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.model.enums.UInformTypeEnum;
import com.lz.user.service.IUserInfoService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR;
import static com.lz.common.constant.config.LocaleConstants.DEFAULT_LOCALE;

/**
 * 图片模块数据生成测试类
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

    @Resource
    private IAccountInfoService accountInfoService;

    @Resource
    private IPointsUsageLogInfoService pointsUsageLogInfoService;

    @Resource
    private com.lz.user.service.IInformInfoService informInfoService;

    @Resource
    private IInformTemplateInfoService informTemplateInfoService;

    // ==================== 全局可配置参数 ====================

    /**
     * 不需要生成数据的用户列表
     */
    private static final List<String> EXCLUDED_USER_IDS = List.of("1", "2", "009");

    /**
     * 不需要生成的空间列表
     */
    private static final List<String> EXCLUDED_SPACE_IDS = List.of(
            "1950538503299981313",
            "1950538697844383745",
            "1950539775252029441",
            "1950582726715973634"
    );

    /**
     * 官方测试空间 ID
     */
    private static final String TEST_SPACE_ID = "2011111829029982210";

    private static final String DOWNLOAD_PICTURE_AUTHOR_PROPORTION_KEY = "download_picture_author_proportion";

    /**
     * 搜索记录目标总数
     */
//    private static final long TARGET_SEARCH_COUNT = 3000_000L;
    private static final long TARGET_SEARCH_COUNT = 1000L;

    /**
     * 空间目标总数
     */
    private static final long TARGET_SPACE_COUNT = 300_000L;

    /**
     * 图片目标总数
     */
    private static final long TARGET_PICTURE_COUNT = 2000_000L;

    /**
     * 下载记录目标总数
     */
    private static final long TARGET_DOWNLOAD_COUNT = 1500_000L;

    /**
     * 用户行为目标总数
     */
    private static final long TARGET_BEHAVIOR_COUNT = 3000_000L;

    /**
     * 比指定时间晚的最小分钟数
     */
    private static final int MIN_MINUTES = 1;

    /**
     * 比指定时间按晚的最大分钟数
     */
    private static final int MAX_MINUTES = 720;

    /**
     * 批量数量
     */
    private static final int BATCH_SIZE = 200000;
    // ==================== 静态数据 ====================

//    private static final List<String> KEYWORDS = List.of(
//            "YY", "Spring", "懒羊羊", "AI", "海绵宝宝", "蜡笔小新", "可爱壁纸", "搞笑表情包", "表情包", "可爱的壁纸", "哆啦A梦",
//            "AI生图", "AI图片生成", "AI图片", "手机壁纸", "电脑高清壁纸", "壁纸", "AI氛围感", "懒羊羊头像", "懒羊羊表情包", "AI生成图片",
//            "小黄鸭表情包", "小黄鸭头像", "城市夜景", "citywork", "广州", "可爱表情包", "搞笑壁纸", "Jay", "Jay Zhou", "Hello kitty",
//            "wallhaven", "聊天背景", "室内特写", "脸部特写", "春节", "熊二", "好运连连", "心想事成", "恭喜发财",
//            "卡通头像", "AI生成头像", "壁纸ins高级质感", "故事宿命感", "自然真实", "大师摄影", "真实人像摄影", "cosplay", "游戏cg", "夕阳", "日出",
//            "蓝调", "猫咪", "Moebius", "头像插画", "极简主义", "复古风格", "艺术风格", "大师作品", "4K", "4k高清"
//    );
    private static final List<String> KEYWORDS = List.of(
        "Spring","AI","壁纸ins高级质感"
    );

    private static final List<String> TAGS = List.of(
            "YY", "Spring", "懒羊羊", "AI", "海绵宝宝", "蜡笔小新", "可爱壁纸", "搞笑表情包", "表情包", "可爱的壁纸", "哆啦A梦",
            "AI生图", "AI图片生成", "AI图片", "手机壁纸", "电脑高清壁纸", "壁纸", "AI氛围感", "懒羊羊头像", "懒羊羊表情包", "AI生成图片",
            "小黄鸭表情包", "小黄鸭头像", "城市夜景", "citywork", "广州", "可爱表情包", "搞笑壁纸", "Jay", "Jay Zhou", "Hello kitty",
            "wallhaven", "聊天背景", "室内特写", "脸部特写", "春节", "小黄鸭", "小心超人", "熊二", "好运连连", "心想事成", "恭喜发财",
            "卡通头像", "AI生成头像", "壁纸ins高级质感", "故事宿命感", "自然真实", "大师摄影", "真实人像摄影", "cosplay", "游戏cg", "夕阳", "日出",
            "蓝调", "猫咪", "Moebius", "头像插画", "极简主义", "复古风格", "艺术风格", "大师作品", "4K", "4k高清", "熊出没", "头像", "即梦AI", "动漫",
            "火影忍者", "学校", "公园", "街景", "图书", "文学", "生活", "电影", "游戏", "音乐", "书籍", "小说", "漫画",
            "movie", "game", "winter", "summer", "spring", "autumn", "street", "city", "park"
    );

    private static final String THUMBNAIL_URL = "/picture/picture/2025/07/31/50b936c6136898cf57689ab4be404ea1-1950595431075549185-compressed.webp";
    private static final String PICTURE_URL = "/picture/picture/2025/07/31/50b936c6136898cf57689ab4be404ea1-1950595431075549184.jpg";
    private static final String SPACE_AVATAR = "/picture/cover/2025/07/30/TTYY-1950539728734523393-compressed.webp";

    // ==================== 数据生成方法 ====================

    @Test
    public void testGenerate() {
        long startTime = System.currentTimeMillis();
        p("开始生成数据...");
        testGenerateSearch();
        testGenerateSpace();
        testGeneratePicture();
        testGenerateDownloadPicture();
        testGenerateBehavior();
        p("数据生成完成", startTime);
    }

    /**
     * 生成搜索记录。
     * - 每个有效用户至少 1 条记录
     * - 按用户注册顺序，越靠前的用户生成越多（比例衰减）
     * - 搜索时间在用户注册后 1 秒 ~ 1 年内
     */
    @Test
    public void testGenerateSearch() {
        long startTime = System.currentTimeMillis();
        p("开始生成搜索记录，目标数量：" + TARGET_SEARCH_COUNT);

        // 1. 加载所有有效用户
        List<UserInfo> userList = userInfoService.list(
                new LambdaQueryWrapper<UserInfo>()
                        .notIn(UserInfo::getUserId, EXCLUDED_USER_IDS)
                        .eq(UserInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                        .orderByDesc(UserInfo::getCreateTime));
        int totalUsers = userList.size();
        if (totalUsers == 0) {
            p("没有可用用户");
            return;
        }

        Map<String, Date> regTimeMap = userList.stream()
                .collect(Collectors.toMap(UserInfo::getUserId, UserInfo::getCreateTime));

        // 2. 计算每个用户应分配的搜索条数（前多后少，总和 ≥ TARGET）
        long[] userAllocation = computeLinearAllocation(TARGET_SEARCH_COUNT, totalUsers);

        // 3. 生成并分批插入
        List<List<SearchLogInfo>> batches = new ArrayList<>();
        List<SearchLogInfo> currentBatch = new ArrayList<>();
        long inserted = 0;
        long lastPrint = 0;

        for (int ui = 0; ui < totalUsers; ui++) {
            UserInfo user = userList.get(ui);
            int count = (int) userAllocation[ui];
            Date regTime = regTimeMap.get(user.getUserId());

            for (int j = 0; j < count; j++) {
                SearchLogInfo log = new SearchLogInfo();
                log.setSearchId(IdUtils.snowflakeId().toString());
                log.setUserId(user.getUserId());
                log.setKeyword(KEYWORDS.get(new Random().nextInt(KEYWORDS.size())));
                log.setSearchType(PSearchTypeEnum.SEARCH_TYPE_0.getValue());
                log.setReferSource(PSearchReferSourceEnum.SEARCH_REFER_SOURCE_0.getValue());
                log.setSearchStatus(PSearchStatusEnum.SEARCH_STATUS_0.getValue());
                log.setFailReason(null);
                log.setResultCount(35L);
//                log.setCreateTime(RandomUtils.generateDateAfter(regTime, MIN_MINUTES, MAX_MINUTES));
                log.setCreateTime(new Date());
                log.setSearchDuration(1000L);
                log.setIpAddr(user.getLastLoginIp());
                log.setIpAddress(user.getIpAddress());
                log.setBrowser("Chrome");
                log.setOs("Windows 10");
                log.setPlatform("Windows");
                log.setDeviceId(null);

                currentBatch.add(log);
                if (currentBatch.size() >= BATCH_SIZE) {
                    batches.add(new ArrayList<>(currentBatch));
                    currentBatch.clear();
                }
                inserted++;
                if (inserted - lastPrint >= 100000) {
                    p("搜索记录生成进度：" + inserted + " / " + TARGET_SEARCH_COUNT + "（" + String.format("%.1f", (double) inserted / TARGET_SEARCH_COUNT * 100) + "%）", startTime);
                    lastPrint = inserted;
                }
            }
        }
        if (!currentBatch.isEmpty()) {
            batches.add(currentBatch);
        }

        p("实际生成搜索记录：" + inserted + "，分 " + batches.size() + " 批，开始写入", startTime);
        saveBatchesParallel(batches, batch -> searchLogInfoService.saveBatch(batch));
        p("搜索记录生成完成", startTime);
    }

    /**
     * 生成空间记录。
     * - 每个有效用户至少 1 个空间
     * - 越靠前的用户生成越多空间（比例衰减）
     * - 空间创建时间在用户注册后 1 秒 ~ 7 天内
     */
    @Test
    public void testGenerateSpace() {
        long startTime = System.currentTimeMillis();
        p("开始生成空间，目标数量：" + TARGET_SPACE_COUNT);

        // 1. 加载所有有效用户
        List<UserInfo> userList = userInfoService.list(
                new LambdaQueryWrapper<UserInfo>()
                        .notIn(UserInfo::getUserId, EXCLUDED_USER_IDS)
                        .eq(UserInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                        .orderByDesc(UserInfo::getCreateTime));
        int totalUsers = userList.size();
        if (totalUsers == 0) {
            p("没有可用用户");
            return;
        }

        Map<String, Date> regTimeMap = userList.stream()
                .collect(Collectors.toMap(UserInfo::getUserId, UserInfo::getCreateTime));

        // 2. 计算每个用户应分配的空间数（前多后少）
        long[] userAllocation = computeLinearAllocation(TARGET_SPACE_COUNT, totalUsers);

        // 3. 生成
        List<List<SpaceInfo>> batches = new ArrayList<>();
        List<SpaceInfo> currentBatch = new ArrayList<>();
        long inserted = 0;
        long lastPrint = 0;

        for (int ui = 0; ui < totalUsers; ui++) {
            UserInfo user = userList.get(ui);
            int count = (int) userAllocation[ui];
            Date regTime = regTimeMap.get(user.getUserId());

            for (int i = 0; i < count; i++) {
                boolean isTeam = (i % 2 == 0);
                SpaceInfo space = new SpaceInfo();
                space.setSpaceId(IdUtils.snowflakeId().toString());
                space.setSpaceName((isTeam ? "团队空间-" : "个人空间-") + ui + "-" + i);
                space.setSpaceAvatar(SPACE_AVATAR);
                space.setMaxSize(1073741824L);
                space.setMaxCount(300L);
                space.setLookCount(0L);
                space.setCollectCount(0L);
                space.setDownloadCount(0L);
                space.setTotalSize(0L);
                space.setTotalCount(0L);
                space.setUserId(user.getUserId());
                space.setSpaceDesc("");
                space.setSpaceStatus(PSpaceStatusEnum.SPACE_STATUS_1.getValue());
                space.setSpaceType(isTeam ? "1" : "2");
                space.setMemberLimit(isTeam ? 10L : 1L);
                space.setCurrentMembers(1L);
                space.setCreateTime(RandomUtils.generateDateAfter(regTime, MIN_MINUTES, MAX_MINUTES));
                space.setLastUpdateTime(null);
                space.setUpdateTime(null);
                space.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
                space.setDeletedTime(null);

                currentBatch.add(space);
                if (currentBatch.size() >= BATCH_SIZE) {
                    batches.add(new ArrayList<>(currentBatch));
                    currentBatch.clear();
                }
                inserted++;
                if (inserted - lastPrint >= BATCH_SIZE) {
                    p("空间生成进度：" + inserted + " / " + TARGET_SPACE_COUNT + "（" + String.format("%.1f", (double) inserted / TARGET_SPACE_COUNT * 100) + "%）", startTime);
                    lastPrint = inserted;
                }
            }
        }
        if (!currentBatch.isEmpty()) {
            batches.add(currentBatch);
        }

        p("实际生成空间：" + inserted + "，分 " + batches.size() + " 批，开始写入", startTime);
        saveBatchesParallel(batches, batch -> spaceInfoService.saveBatch(batch));
        p("空间生成完成", startTime);
    }

    /**
     * 生成图片记录。
     * - 每个有效空间至少 1 张图片
     * - 越靠前的空间生成越多图片（比例衰减）
     * - 图片上传时间在空间创建后 1 秒 ~ 30 天内
     */
    @Test
    public void testGeneratePicture() {
        long startTime = System.currentTimeMillis();
        p("开始生成图片，目标数量：" + TARGET_PICTURE_COUNT);

        // 0. 确保标签存在
        ensureTagsExist();

        // 1. 加载有效空间
        List<SpaceInfo> spaceList = spaceInfoService.list(
                new LambdaQueryWrapper<SpaceInfo>()
                        .notIn(SpaceInfo::getSpaceId, EXCLUDED_SPACE_IDS)
                        .notIn(SpaceInfo::getUserId, EXCLUDED_USER_IDS)
                        .eq(SpaceInfo::getSpaceType, PSpaceTypeEnum.SPACE_TYPE_1.getValue())
                        .orderByDesc(SpaceInfo::getCreateTime));
        int totalSpaces = spaceList.size();
        if (totalSpaces == 0) {
            p("没有可用空间");
            return;
        }

        // 2. 收集空间所属用户的注册时间
        Set<String> ownerIds = new HashSet<>();
        for (SpaceInfo s : spaceList) {
            ownerIds.add(s.getUserId());
        }
        Map<String, Date> ownerRegTimeMap = new HashMap<>();
        List<UserInfo> owners = userInfoService.list(
                new LambdaQueryWrapper<UserInfo>().in(UserInfo::getUserId, ownerIds));
        ownerRegTimeMap = owners.stream()
                .collect(Collectors.toMap(UserInfo::getUserId, UserInfo::getCreateTime));

        // 3. 加载分类
        List<PictureCategoryInfo> categoryList = pictureCategoryInfoService.list(
                new LambdaQueryWrapper<PictureCategoryInfo>()
                        .ne(PictureCategoryInfo::getCategoryId, "0"));

        // 4. 加载标签
        List<PictureTagInfo> tagList = pictureTagInfoService.list();

        // 5. 计算每个空间应分配的图片数
        long[] spaceAllocation = computeLinearAllocation(TARGET_PICTURE_COUNT, totalSpaces);

        // 6. 生成
        List<SpaceInfo> updatedSpaces = new ArrayList<>();
        int minSize = 1024 * 1024;
        int maxSize = 1024 * 1024 * 10;
        Random random = new Random();
        long inserted = 0;
        long lastPrint = 0;
        List<PictureInfo> currentPicBatch = new ArrayList<>();
        List<PictureTagRelInfo> currentTagBatch = new ArrayList<>();
        Map<String, PictureTagInfo> tagCountMap = new HashMap<>();
        String moreInfo = """
                {"applyType":"1","pointsNeed":10,"priceNeed":0}
                """;
        for (int si = 0; si < totalSpaces; si++) {
            SpaceInfo space = spaceList.get(si);
            int picCount = (int) spaceAllocation[si];
            Date ownerRegTime = ownerRegTimeMap.getOrDefault(space.getUserId(), space.getCreateTime());
            Date spaceCreateTime = space.getCreateTime();
            long totalSize = space.getTotalSize() != null ? space.getTotalSize() : 0L;
            long totalCount = space.getTotalCount() != null ? space.getTotalCount() : 0L;

            for (int i = 0; i < picCount; i++) {
                PictureCategoryInfo category = categoryList.get(random.nextInt(categoryList.size()));
                Date uploadTime = spaceCreateTime != null
                        ? RandomUtils.generateDateAfter(spaceCreateTime, MIN_MINUTES, MAX_MINUTES)
                        : RandomUtils.generateDateAfter(ownerRegTime, MIN_MINUTES, MAX_MINUTES);

                long picSize = random.nextLong(minSize, maxSize);
                PictureInfo pic = new PictureInfo();
                pic.setPictureId(IdUtils.snowflakeId().toString());
                pic.setPictureUrl(PICTURE_URL);
                pic.setName(category.getName() + space.getSpaceName() + "-" + i);
                pic.setIntroduction("");
                pic.setCategoryId(category.getCategoryId());
                pic.setPicSize(picSize);
                pic.setPicWidth(1259);
                pic.setPicHeight(1259);
                pic.setPicScale(1.0);
                pic.setPicFormat("jpg");
                pic.setUserId(space.getUserId());
                pic.setCreateTime(uploadTime);
                pic.setPictureStatus(PPictureStatusEnum.PICTURE_STATUS_0.getValue());
                pic.setThumbnailUrl(THUMBNAIL_URL);
                pic.setLookCount(0L);
                pic.setCollectCount(0L);
                pic.setLikeCount(0L);
                pic.setShareCount(0L);
                pic.setDownloadCount(0L);
                pic.setSpaceId(space.getSpaceId());
                pic.setUploadType(String.valueOf((Long.parseLong(space.getSpaceId()) % 3) + 1));
                pic.setIsDelete("1");
                pic.setMoreInfo(moreInfo);

                currentPicBatch.add(pic);
                totalSize += picSize;
                totalCount++;
                inserted++;
                if (inserted - lastPrint >= BATCH_SIZE) {
                    p("图片生成进度：" + inserted + " / " + TARGET_PICTURE_COUNT + "（" + String.format("%.1f", (double) inserted / TARGET_PICTURE_COUNT * 100) + "%）", startTime);
                    lastPrint = inserted;
                }

                // 标签关联
                Set<Integer> chosenTags = new HashSet<>();
                int maxTags = Math.min(5, tagList.size());
                while (chosenTags.size() < maxTags) {
                    chosenTags.add(random.nextInt(tagList.size()));
                }
                for (Integer idx : chosenTags) {
                    PictureTagInfo tag = tagList.get(idx);
                    tagCountMap.computeIfAbsent(tag.getTagId(), k -> tag).setUsageCount(tag.getUsageCount() + 1);
                    PictureTagRelInfo rel = new PictureTagRelInfo();
                    rel.setPictureId(pic.getPictureId());
                    rel.setTagId(tag.getTagId());
                    rel.setTagName(tag.getName());
                    rel.setPictureName(pic.getName());
                    rel.setLookCount(0L);
                    rel.setCollectCount(0L);
                    rel.setLikeCount(0L);
                    rel.setShareCount(0L);
                    rel.setDownloadCount(0L);
                    rel.setUserId(space.getUserId());
                    rel.setCreateTime(uploadTime);
                    currentTagBatch.add(rel);
                }

                // 达到一批就立即写入，释放内存
                if (currentPicBatch.size() >= BATCH_SIZE) {
                    pictureInfoService.saveBatch(new ArrayList<>(currentPicBatch));
                    pictureTagRelInfoService.saveBatch(new ArrayList<>(currentTagBatch));
                    currentPicBatch.clear();
                    currentTagBatch.clear();
                }
            }

            space.setTotalSize(totalSize);
            space.setTotalCount(totalCount);
            updatedSpaces.add(space);
        }

        // 保存剩余不足一批的数据
        if (!currentPicBatch.isEmpty()) {
            pictureInfoService.saveBatch(currentPicBatch);
            pictureTagRelInfoService.saveBatch(currentTagBatch);
        }

        p("实际生成图片：" + inserted + "，开始写入", startTime);

        // 更新空间和标签统计
        spaceInfoService.updateBatchById(updatedSpaces);
        pictureTagInfoService.updateBatchById(new ArrayList<>(tagCountMap.values()));

        p("图片生成完成", startTime);
    }

    /**
     * 生成下载记录（带完整业务逻辑）。
     * - 每个有效用户至少 1 条记录
     * - 越靠前的用户生成越多记录（比例衰减）
     * - 下载时间必须在用户注册时间之后，且在图片上传时间之后
     * - 解析图片 moreInfo 获取所需积分，模拟完整下载流程：
     * 1. 判断下载者是否为作者/空间成员（免费）
     * 2. 非作者：扣减下载者积分，生成积分消费记录
     * 3. 非免费且积分>=10：给作者增加积分，生成作者积分提成记录
     * 4. 更新：图片下载次数、分类下载次数、空间下载次数、标签关联下载次数、标签下载次数
     * - 所有写操作按批次同步执行，不占用大量内存
     */
    @Test
    public void testGenerateDownloadPicture() {
        long startTime = System.currentTimeMillis();
        p("开始生成下载记录，目标数量：" + TARGET_DOWNLOAD_COUNT);

        // 下载分成比例
        double PICTURE_DOWNLOAD_OFFICIAL_PROPORTION_VALUE = 0.3;
        double PICTURE_DOWNLOAD_SPACE_PROPORTION_VALUE = 0.2;

        // 1. 加载有效用户和有效图片
        List<UserInfo> userList = userInfoService.list(
                new LambdaQueryWrapper<UserInfo>()
                        .notIn(UserInfo::getUserId, EXCLUDED_USER_IDS)
                        .eq(UserInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                        .orderByDesc(UserInfo::getCreateTime));
        int totalUsers = userList.size();
        if (totalUsers == 0) {
            p("没有可用用户");
            return;
        }

        Map<String, Date> userRegTimeMap = userList.stream()
                .collect(Collectors.toMap(UserInfo::getUserId, UserInfo::getCreateTime));

        // 加载有效图片
        List<PictureInfo> picList = pictureInfoService.list(
                new LambdaQueryWrapper<PictureInfo>()
                        .notIn(PictureInfo::getUserId, EXCLUDED_USER_IDS)
                        .notIn(PictureInfo::getSpaceId, EXCLUDED_SPACE_IDS)
                        .last("limit 5000"));
        if (picList.isEmpty()) {
            p("没有可用图片，请先生成图片");
            return;
        }

        // 加载提成通知模板（一次性，避免循环内重复查询）
        InformTemplateInfo authorInformTemplate = informTemplateInfoService.getInformTemplateInfoByKeyLocaleType(
                DOWNLOAD_PICTURE_AUTHOR_PROPORTION_KEY, DEFAULT_LOCALE, CTemplateTypeEnum.TEMPLATE_TYPE_3.getValue());

        Map<String, List<String>> picTagMap = buildPicTagMap(picList);
        Map<String, Date> picUploadTimeMap = picList.stream()
                .collect(Collectors.toMap(PictureInfo::getPictureId, PictureInfo::getCreateTime));
        // 图片 -> 作者ID
        Map<String, String> picAuthorMap = picList.stream()
                .collect(Collectors.toMap(PictureInfo::getPictureId, PictureInfo::getUserId));
        // 图片 -> moreInfo 解析出的所需积分
        Map<String, Long> picPointsNeedMap = new HashMap<>();
        for (PictureInfo pic : picList) {
            Long pointsNeed = 0L;
            if (pic.getMoreInfo() != null && !pic.getMoreInfo().isBlank()) {
                try {
                    PictureMoreInfo moreInfo = com.alibaba.fastjson2.JSON.parseObject(
                            pic.getMoreInfo(), PictureMoreInfo.class);
                    if (moreInfo != null && moreInfo.getPointsNeed() != null) {
                        pointsNeed = moreInfo.getPointsNeed();
                    }
                } catch (Exception ignored) {
                }
            }
            picPointsNeedMap.put(pic.getPictureId(), pointsNeed);
        }

        // 加载空间成员关系（spaceId -> Set<userId>）
        Map<String, Set<String>> spaceMemberMap = new HashMap<>();
        for (SpaceInfo s : spaceInfoService.list()) {
            spaceMemberMap.computeIfAbsent(s.getSpaceId(), k -> new HashSet<>()).add(s.getUserId());
        }

        // 加载所有用户账户
        List<AccountInfo> accountList = accountInfoService.list(
                new LambdaQueryWrapper<com.lz.points.model.domain.AccountInfo>()
                        .notIn(com.lz.points.model.domain.AccountInfo::getUserId, EXCLUDED_USER_IDS));
        Map<String, AccountInfo> accountMap = accountList.stream()
                .collect(Collectors.toMap(AccountInfo::getUserId, a -> a));

        // 2. 计算每个用户的分配数
        long[] userAllocation = computeLinearAllocation(TARGET_DOWNLOAD_COUNT, totalUsers);

        // 3. 生成批次
        List<PictureDownloadLogInfo> currentDownloadBatch = new ArrayList<>();
        List<PointsUsageLogInfo> currentUsageBatch = new ArrayList<>();
        List<PointsUsageLogInfo> currentAuthorBatch = new ArrayList<>();
        List<InformInfo> currentInformBatch = new ArrayList<>();
        long inserted = 0;
        long lastPrint = 0;
        Random random = new Random();

        // 统计计数
        Map<String, Long> picDownloadCountMap = new HashMap<>();
        Map<String, Long> categoryDownloadCountMap = new HashMap<>();
        Map<String, Long> spaceDownloadCountMap = new HashMap<>();
        Map<String, Long> tagNameCountMap = new HashMap<>();
        Map<String, Long> tagRelCountMap = new HashMap<>();

        for (int ui = 0; ui < totalUsers; ui++) {
            UserInfo user = userList.get(ui);
            int count = (int) userAllocation[ui];
            Date userRegTime = userRegTimeMap.get(user.getUserId());
            String userId = user.getUserId();
            String ipAddr = user.getLastLoginIp();
            String ipAddress = user.getIpAddress();

            AccountInfo userAccount = accountMap.get(userId);

            for (int i = 0; i < count; i++) {
                PictureInfo pic = picList.get(random.nextInt(picList.size()));
                String picId = pic.getPictureId();
                String authorId = picAuthorMap.get(picId);
                String categoryId = pic.getCategoryId();
                String spaceId = pic.getSpaceId();
                Date picUploadTime = picUploadTimeMap.get(picId);

                // 下载时间：必须在用户注册之后，且在图片上传之后
                Date downloadTime = RandomUtils.generateDateAfterSeconds(userRegTime, MIN_MINUTES, MAX_MINUTES);
                if (picUploadTime != null && downloadTime.before(picUploadTime)) {
                    downloadTime = RandomUtils.generateDateAfter(picUploadTime, MIN_MINUTES, MAX_MINUTES);
                }

                // 获取图片所需积分
                Long pointsNeed = picPointsNeedMap.getOrDefault(picId, 0L);
                if (pointsNeed == null) pointsNeed = 0L;

                PictureDownloadLogInfo log = new PictureDownloadLogInfo();
                log.setDownloadId(IdUtils.snowflakeId().toString());
                log.setUserId(userId);
                log.setPictureId(picId);
                log.setCategoryId(categoryId);
                log.setPictureName(pic.getName() + "." + pic.getPicFormat());
                log.setThumbnailUrl(pic.getThumbnailUrl());
                List<String> tags = picTagMap.get(picId);
                if (tags != null && !tags.isEmpty()) {
                    log.setTags(String.join(COMMON_SEPARATOR, tags));
                }
                log.setSpaceId(spaceId);
                log.setCreateTime(downloadTime);
                log.setDownloadStatus(PDownloadStatusEnum.DOWNLOAD_STATUS_0.getValue());
                log.setFailReason(null);
                log.setDownloadType(PDownloadTypeEnum.DOWNLOAD_TYPE_1.getValue());
                log.setReferSource(PDownloadReferSourceEnum.DOWNLOAD_REFER_SOURCE_0.getValue());
                log.setHasStatistics(CommonHasStatisticsEnum.HAS_STATISTICS_1.getValue());
                log.setIpAddr(ipAddr);
                log.setIpAddress(ipAddress);
                log.setDeviceId(null);
                log.setBrowser("Chrome");
                log.setOs("Windows 10");
                log.setPlatform("Windows");

                // 判断是否为作者或空间成员（免费）
                boolean isAuthor = authorId != null && authorId.equals(userId);
                Set<String> members = spaceMemberMap.getOrDefault(spaceId, Collections.emptySet());
                boolean isSpaceMember = members.contains(userId);

                if (isAuthor || isSpaceMember) {
                    // 免费：作者本人或空间成员
                    log.setPointsCost(0L);
                    log.setPointsAuthorGain(0L);
                    log.setPointsOfficialGain(0L);
                    log.setPointsSpaceGain(0L);
                    log.setAuthorProportion(BigDecimal.valueOf(0.0));
                    log.setOfficialProportion(BigDecimal.valueOf(0.0));
                    log.setSpaceProportion(BigDecimal.valueOf(0.0));
                    log.setScore(0.0);
                } else {
                    // 非作者，需要扣积分
                    Long userBalance = userAccount != null ? userAccount.getPointsBalance() : 0L;

                    if (pointsNeed > 0 && userBalance >= pointsNeed) {
                        // 余额充足，真实扣减
                        userAccount.setPointsBalance(userBalance - pointsNeed);
                        userAccount.setPointsUsed(
                                (userAccount.getPointsUsed() != null ? userAccount.getPointsUsed() : 0L) + pointsNeed);

                        PointsUsageLogInfo usageLog = new PointsUsageLogInfo();
                        usageLog.setLogId(IdUtils.snowflakeId().toString());
                        usageLog.setUserId(userId);
                        usageLog.setGiveUserId(null);
                        usageLog.setLogType(PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_1.getValue());
                        usageLog.setUsageType(PoPointsUsageTypeEnum.POINTS_USAGE_TYPE_0.getValue());
                        usageLog.setTargetId(picId);
                        usageLog.setPointsBefore(userBalance);
                        usageLog.setPointsUsed(pointsNeed);
                        usageLog.setPointsAfter(userBalance - pointsNeed);
                        usageLog.setBrowser("Chrome");
                        usageLog.setOs("Windows 10");
                        usageLog.setPlatform("Windows");
                        usageLog.setIpAddr(ipAddr);
                        usageLog.setIpAddress(ipAddress);
                        usageLog.setCreateTime(downloadTime);
                        usageLog.setUpdateTime(downloadTime);
                        usageLog.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
                        currentUsageBatch.add(usageLog);

                        log.setPointsCost(pointsNeed);

                        // 分成：>=10 且为10的倍数才分成
                        if (pointsNeed >= 10 && pointsNeed % 10 == 0) {
                            double authorProp = 1 - PICTURE_DOWNLOAD_OFFICIAL_PROPORTION_VALUE - PICTURE_DOWNLOAD_SPACE_PROPORTION_VALUE;
                            long authorGain = (long) (pointsNeed * authorProp);
                            long officialGain = (long) (pointsNeed * PICTURE_DOWNLOAD_OFFICIAL_PROPORTION_VALUE);
                            long spaceGain = (long) (pointsNeed * PICTURE_DOWNLOAD_SPACE_PROPORTION_VALUE);

                            log.setPointsAuthorGain(authorGain);
                            log.setPointsOfficialGain(officialGain);
                            log.setPointsSpaceGain(spaceGain);
                            log.setAuthorProportion(BigDecimal.valueOf(authorProp));
                            log.setOfficialProportion(BigDecimal.valueOf(PICTURE_DOWNLOAD_OFFICIAL_PROPORTION_VALUE));
                            log.setSpaceProportion(BigDecimal.valueOf(PICTURE_DOWNLOAD_SPACE_PROPORTION_VALUE));
                            log.setScore(20.0);

                            // 给作者增加积分，生成提成记录
                            AccountInfo authorAccount = accountMap.get(authorId);
                            if (authorAccount != null) {
                                Long authorBalanceBefore = authorAccount.getPointsBalance() != null
                                        ? authorAccount.getPointsBalance() : 0L;
                                authorAccount.setPointsBalance(authorBalanceBefore + authorGain);
                                authorAccount.setPointsEarned(
                                        (authorAccount.getPointsEarned() != null ? authorAccount.getPointsEarned() : 0L) + authorGain);

                                PointsUsageLogInfo authorUsageLog = new PointsUsageLogInfo();
                                authorUsageLog.setLogId(IdUtils.snowflakeId().toString());
                                authorUsageLog.setUserId(authorId);
                                authorUsageLog.setGiveUserId(userId);
                                authorUsageLog.setLogType(PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_2.getValue());
                                authorUsageLog.setUsageType(PoPointsUsageTypeEnum.POINTS_USAGE_TYPE_0.getValue());
                                authorUsageLog.setTargetId(picId);
                                authorUsageLog.setPointsBefore(authorBalanceBefore);
                                authorUsageLog.setPointsUsed(-authorGain);
                                authorUsageLog.setPointsAfter(authorBalanceBefore + authorGain);
                                authorUsageLog.setBrowser("Chrome");
                                authorUsageLog.setOs("Windows 10");
                                authorUsageLog.setPlatform("Windows");
                                authorUsageLog.setIpAddr(ipAddr);
                                authorUsageLog.setIpAddress(ipAddress);
                                authorUsageLog.setCreateTime(downloadTime);
                                authorUsageLog.setUpdateTime(downloadTime);
                                authorUsageLog.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
                                currentAuthorBatch.add(authorUsageLog);

                                // 批量插入通知记录（直接构造 InformInfo，不走 sendInform 查模板）
                                if (authorInformTemplate != null) {
                                    com.lz.user.model.domain.InformInfo informInfo = new com.lz.user.model.domain.InformInfo();
                                    informInfo.setRecordId(IdUtils.snowflakeId().toString());
                                    informInfo.setTemplateKey(authorInformTemplate.getTemplateKey());
                                    informInfo.setTemplateType(authorInformTemplate.getTemplateType());
                                    informInfo.setLocale(authorInformTemplate.getLocale());
                                    informInfo.setInformTitle(authorInformTemplate.getInformTitle());
                                    informInfo.setUserId(authorId);
                                    HashMap<String, String> informParams = new HashMap<>();
                                    informParams.put("points", String.valueOf(authorGain));
                                    informParams.put("pictureName", pic.getName());
                                    informParams.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(downloadTime));
                                    informInfo.setContent(StringUtils.parseTemplate(authorInformTemplate.getContent(), informParams));
                                    informInfo.setInformType(UInformTypeEnum.INFORM_TYPE_0.getValue());
                                    informInfo.setStatus("1");
                                    informInfo.setIsRead("0");
                                    informInfo.setRetryCount(0L);
                                    informInfo.setSendTime(downloadTime);
                                    informInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
                                    currentInformBatch.add(informInfo);
                                }
                            } else {
                                log.setPointsAuthorGain(0L);
                                log.setScore(0.0);
                            }
                        } else {
                            // 不满足分成条件
                            log.setPointsAuthorGain(0L);
                            log.setPointsOfficialGain(0L);
                            log.setPointsSpaceGain(0L);
                            log.setAuthorProportion(BigDecimal.valueOf(0.0));
                            log.setOfficialProportion(BigDecimal.valueOf(0.0));
                            log.setSpaceProportion(BigDecimal.valueOf(0.0));
                            log.setScore(0.0);
                        }
                    } else {
                        // 余额不足或积分为0，走免费
                        log.setPointsCost(0L);
                        log.setPointsAuthorGain(0L);
                        log.setPointsOfficialGain(0L);
                        log.setPointsSpaceGain(0L);
                        log.setAuthorProportion(BigDecimal.valueOf(0.0));
                        log.setOfficialProportion(BigDecimal.valueOf(0.0));
                        log.setSpaceProportion(BigDecimal.valueOf(0.0));
                        log.setScore(0.0);
                    }
                }

                currentDownloadBatch.add(log);

                // 统计计数
                picDownloadCountMap.merge(picId, 1L, Long::sum);
                if (categoryId != null) categoryDownloadCountMap.merge(categoryId, 1L, Long::sum);
                if (spaceId != null) spaceDownloadCountMap.merge(spaceId, 1L, Long::sum);
                List<String> tagNames = picTagMap.get(picId);
                if (tagNames != null) {
                    for (String tagName : tagNames) {
                        tagNameCountMap.merge(tagName, 1L, Long::sum);
                        tagRelCountMap.merge(picId + "|" + tagName, 1L, Long::sum);
                    }
                }

                inserted++;
                if (inserted - lastPrint >= BATCH_SIZE) {
                    p("下载记录生成进度：" + inserted + " / " + TARGET_DOWNLOAD_COUNT + "（" + String.format("%.1f", (double) inserted / TARGET_DOWNLOAD_COUNT * 100) + "%）", startTime);
                    lastPrint = inserted;
                }

                // 满批写入
                if (currentDownloadBatch.size() >= BATCH_SIZE) {
                    flushDownloadBatch(currentDownloadBatch, currentUsageBatch, currentAuthorBatch, currentInformBatch, accountMap);
                    currentDownloadBatch.clear();
                    currentUsageBatch.clear();
                    currentAuthorBatch.clear();
                    currentInformBatch.clear();
                }
            }
        }

        // 写剩余批次
        if (!currentDownloadBatch.isEmpty()) {
            flushDownloadBatch(currentDownloadBatch, currentUsageBatch, currentAuthorBatch, currentInformBatch, accountMap);
        }

        p("实际生成下载记录：" + inserted + "，开始写入", startTime);
        // 更新统计计数（分批处理，避免 OOM）
        p("开始更新下载统计计数", startTime);
        updateDownloadCounts(picDownloadCountMap, categoryDownloadCountMap, spaceDownloadCountMap, tagNameCountMap, tagRelCountMap);
        p("下载记录生成完成", startTime);
    }

    /**
     * 将一批下载记录、消费记录、作者提成记录写入数据库。
     * 同时把内存中累计的账户变动（下载者扣积分、作者加积分）同步落库。
     */
    private void flushDownloadBatch(List<PictureDownloadLogInfo> downloadBatch,
                                    List<PointsUsageLogInfo> usageBatch,
                                    List<PointsUsageLogInfo> authorBatch,
                                    List<com.lz.user.model.domain.InformInfo> informBatch,
                                    Map<String, AccountInfo> accountMap) {
        pictureDownloadLogInfoService.saveBatch(downloadBatch);
        if (!usageBatch.isEmpty()) {
            pointsUsageLogInfoService.saveBatch(usageBatch);
        }
        if (!authorBatch.isEmpty()) {
            pointsUsageLogInfoService.saveBatch(authorBatch);
        }
        if (!informBatch.isEmpty()) {
            informInfoService.saveBatch(informBatch);
        }
        // 收集本批涉及的账户ID并批量更新
        Set<String> affectedUserIds = new HashSet<>();
        for (PictureDownloadLogInfo log : downloadBatch) {
            if (log.getPointsCost() != null && log.getPointsCost() > 0) {
                affectedUserIds.add(log.getUserId());
            }
        }
        for (PointsUsageLogInfo ul : authorBatch) {
            if (ul.getUserId() != null) {
                affectedUserIds.add(ul.getUserId());
            }
        }
        if (!affectedUserIds.isEmpty()) {
            List<AccountInfo> toUpdate = affectedUserIds.stream()
                    .map(accountMap::get)
                    .filter(Objects::nonNull)
                    .toList();
            if (!toUpdate.isEmpty()) {
                accountInfoService.updateBatchById(toUpdate);
            }
        }
    }

    /**
     * 批量更新图片、分类、空间、标签、标签关联的下载次数（增量累加）。
     * 分批处理，避免 OOM。
     */
    private void updateDownloadCounts(Map<String, Long> picCountMap,
                                      Map<String, Long> categoryCountMap,
                                      Map<String, Long> spaceCountMap,
                                      Map<String, Long> tagNameCountMap,
                                      Map<String, Long> tagRelCountMap) {
        // 更新图片下载次数
        for (Map.Entry<String, Long> e : picCountMap.entrySet()) {
            pictureInfoService.update(null,
                    new UpdateWrapper<PictureInfo>()
                            .eq("picture_id", e.getKey())
                            .setSql("download_count = download_count + " + e.getValue()));
        }

        // 更新分类下载次数
        for (Map.Entry<String, Long> e : categoryCountMap.entrySet()) {
            pictureCategoryInfoService.update(null,
                    new UpdateWrapper<PictureCategoryInfo>()
                            .eq("category_id", e.getKey())
                            .setSql("download_count = download_count + " + e.getValue()));
        }

        // 更新空间下载次数
        for (Map.Entry<String, Long> e : spaceCountMap.entrySet()) {
            spaceInfoService.update(null,
                    new UpdateWrapper<SpaceInfo>()
                            .eq("space_id", e.getKey())
                            .setSql("download_count = download_count + " + e.getValue()));
        }

        // 更新标签下载次数（只加载本次涉及的标签，不全表）
        if (!tagNameCountMap.isEmpty()) {
            List<PictureTagInfo> tagList = pictureTagInfoService.list(
                    new LambdaQueryWrapper<PictureTagInfo>()
                            .in(PictureTagInfo::getName, tagNameCountMap.keySet()));
            for (PictureTagInfo tag : tagList) {
                Long cnt = tagNameCountMap.getOrDefault(tag.getName(), 0L);
                tag.setDownloadCount(cnt);
            }
            pictureTagInfoService.updateBatchById(tagList);
        }

        // 更新标签关联下载次数（分批按 picId 查找，不全表）
        if (!tagRelCountMap.isEmpty()) {
            int batch = 500;
            List<String> picIds = new ArrayList<>(tagRelCountMap.keySet().stream().map(k -> k.split("\\|")[0]).toList());
            List<String> distinctPicIds = picIds.stream().distinct().toList();
            for (int i = 0; i < distinctPicIds.size(); i += batch) {
                List<String> batchPicIds = distinctPicIds.subList(i, Math.min(i + batch, distinctPicIds.size()));
                List<PictureTagRelInfo> relList = pictureTagRelInfoService.list(
                        new LambdaQueryWrapper<PictureTagRelInfo>()
                                .in(PictureTagRelInfo::getPictureId, batchPicIds));
                List<PictureTagRelInfo> toUpdate = new ArrayList<>();
                for (PictureTagRelInfo rel : relList) {
                    Long cnt = tagRelCountMap.getOrDefault(rel.getPictureId() + "|" + rel.getTagName(), 0L);
                    if (cnt > 0) {
                        rel.setDownloadCount(cnt);
                        toUpdate.add(rel);
                    }
                }
                if (!toUpdate.isEmpty()) {
                    pictureTagRelInfoService.updateBatchById(toUpdate);
                }
            }
        }
    }

    /**
     * 生成用户行为记录。
     * - 每个有效用户至少 1 条记录
     * - 越靠前的用户生成越多记录（比例衰减）
     * - 行为时间必须在用户注册时间之后，且在图片上传时间之后
     */
    @Test
    public void testGenerateBehavior() {
        long startTime = System.currentTimeMillis();
        p("开始生成用户行为，目标数量：" + TARGET_BEHAVIOR_COUNT);

        // 1. 加载有效用户和有效图片
        List<UserInfo> userList = userInfoService.list(
                new LambdaQueryWrapper<UserInfo>()
                        .notIn(UserInfo::getUserId, EXCLUDED_USER_IDS)
                        .eq(UserInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                        .orderByDesc(UserInfo::getCreateTime));
        int totalUsers = userList.size();
        if (totalUsers == 0) {
            p("没有可用用户");
            return;
        }

        Map<String, Date> userRegTimeMap = userList.stream()
                .collect(Collectors.toMap(UserInfo::getUserId, UserInfo::getCreateTime));

        List<PictureInfo> picList = pictureInfoService.list(
                new LambdaQueryWrapper<PictureInfo>()
                        .notIn(PictureInfo::getUserId, EXCLUDED_USER_IDS)
                        .notIn(PictureInfo::getSpaceId, EXCLUDED_SPACE_IDS)
                        .last("limit "+BATCH_SIZE));
        if (picList.isEmpty()) {
            p("没有可用图片，请先生成图片");
            return;
        }

        Map<String, List<String>> picTagMap = buildPicTagMap(picList);
        Map<String, Date> picUploadTimeMap = picList.stream()
                .collect(Collectors.toMap(PictureInfo::getPictureId, PictureInfo::getCreateTime));

        List<String> behaviorTypes = Arrays.stream(PUserBehaviorTypeEnum.values())
                .map(PUserBehaviorTypeEnum::getValue)
                .toList();

        // 2. 计算每个用户的分配数
        long[] userAllocation = computeLinearAllocation(TARGET_BEHAVIOR_COUNT, totalUsers);

        // 3. 生成
        List<List<UserBehaviorInfo>> batches = new ArrayList<>();
        List<UserBehaviorInfo> currentBatch = new ArrayList<>();
        long inserted = 0;
        long lastPrint = 0;
        Random random = new Random();

        for (int ui = 0; ui < totalUsers; ui++) {
            UserInfo user = userList.get(ui);
            int count = (int) userAllocation[ui];
            Date userRegTime = userRegTimeMap.get(user.getUserId());

            for (int i = 0; i < count; i++) {
                PictureInfo pic = picList.get(random.nextInt(picList.size()));
                Date picUploadTime = picUploadTimeMap.get(pic.getPictureId());

                // 行为时间：必须在用户注册之后，且在图片上传之后
                Date behaviorTime = RandomUtils.generateDateAfterSeconds(
                        userRegTime, MIN_MINUTES, MAX_MINUTES);
                if (picUploadTime != null && behaviorTime.before(picUploadTime)) {
                    behaviorTime = RandomUtils.generateDateAfter(picUploadTime, MIN_MINUTES, MAX_MINUTES);
                }

                UserBehaviorInfo info = new UserBehaviorInfo();
                info.setBehaviorId(IdUtils.snowflakeId().toString());
                info.setBehaviorType(behaviorTypes.get(random.nextInt(behaviorTypes.size())));
                info.setUserId(user.getUserId());
                info.setTargetType(PUserBehaviorTargetTypeEnum.USER_BEHAVIOR_TARGET_TYPE_0.getValue());
                info.setTargetId(pic.getPictureId());
                info.setTargetContent(pic.getName());
                info.setScore(0.0);
                info.setShareLink("");
                info.setCategoryId(pic.getCategoryId());
                info.setSpaceId(pic.getSpaceId());
                List<String> tags = picTagMap.get(pic.getPictureId());
                if (tags != null && !tags.isEmpty()) {
                    info.setTags(String.join(COMMON_SEPARATOR, tags));
                }
                info.setTargetCover(pic.getThumbnailUrl());
                info.setCreateTime(behaviorTime);
                info.setHasStatistics(CommonHasStatisticsEnum.HAS_STATISTICS_0.getValue());
                info.setDeviceId(null);
                info.setBrowser("Chrome");
                info.setOs("Windows 11");
                info.setPlatform("Windows");
                info.setIpAddr(user.getLastLoginIp());
                info.setIpAddress(user.getIpAddress());

                currentBatch.add(info);
                if (currentBatch.size() >= BATCH_SIZE) {
                    batches.add(new ArrayList<>(currentBatch));
                    currentBatch.clear();
                }
                inserted++;
                if (inserted - lastPrint >= BATCH_SIZE) {
                    p("用户行为生成进度：" + inserted + " / " + TARGET_BEHAVIOR_COUNT + "（" + String.format("%.1f", (double) inserted / TARGET_BEHAVIOR_COUNT * 100) + "%）", startTime);
                    lastPrint = inserted;
                }
            }
        }
        if (!currentBatch.isEmpty()) {
            batches.add(currentBatch);
        }

        p("实际生成用户行为：" + inserted + "，分 " + batches.size() + " 批，开始写入", startTime);
        saveBatchesParallel(batches, batch -> userBehaviorInfoService.saveBatch(batch));
        p("用户行为生成完成", startTime);
    }

    /**
     * 生成用户浏览记录。
     * - 每个有效用户至少 1 条记录
     * - 越靠前的用户生成越多记录（比例衰减）
     * - 浏览时间必须在用户注册时间之后，且在图片上传时间之后
     */
    //当前不需要
    @Test
    public void testGenerateViewLog() {
        long startTime = System.currentTimeMillis();
        p("开始生成浏览记录，目标数量：" + TARGET_BEHAVIOR_COUNT);

        // 1. 加载有效用户
        List<UserInfo> userList = userInfoService.list(
                new LambdaQueryWrapper<UserInfo>()
                        .notIn(UserInfo::getUserId, EXCLUDED_USER_IDS)
                        .eq(UserInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                        .orderByDesc(UserInfo::getCreateTime));
        int totalUsers = userList.size();
        if (totalUsers == 0) {
            p("没有可用用户");
            return;
        }

        Map<String, Date> userRegTimeMap = userList.stream()
                .collect(Collectors.toMap(UserInfo::getUserId, UserInfo::getCreateTime));

        List<PictureInfo> picList = pictureInfoService.list(
                new LambdaQueryWrapper<PictureInfo>()
                        .notIn(PictureInfo::getUserId, EXCLUDED_USER_IDS)
                        .notIn(PictureInfo::getSpaceId, EXCLUDED_SPACE_IDS)
                        .last("limit 5000"));
        if (picList.isEmpty()) {
            p("没有可用图片，请先生成图片");
            return;
        }

        Map<String, List<String>> picTagMap = buildPicTagMap(picList);
        Map<String, Date> picUploadTimeMap = picList.stream()
                .collect(Collectors.toMap(PictureInfo::getPictureId, PictureInfo::getCreateTime));

        // 2. 计算每个用户的分配数
        long[] userAllocation = computeLinearAllocation(TARGET_BEHAVIOR_COUNT, totalUsers);

        // 3. 生成
        List<List<UserViewLogInfo>> batches = new ArrayList<>();
        List<UserViewLogInfo> currentBatch = new ArrayList<>();
        long inserted = 0;
        Random random = new Random();

        for (int ui = 0; ui < totalUsers; ui++) {
            UserInfo user = userList.get(ui);
            int count = (int) userAllocation[ui];
            Date userRegTime = userRegTimeMap.get(user.getUserId());

            for (int i = 0; i < count; i++) {
                PictureInfo pic = picList.get(random.nextInt(picList.size()));
                Date picUploadTime = picUploadTimeMap.get(pic.getPictureId());

                // 浏览时间：必须在用户注册之后，且在图片上传之后
                Date viewTime = RandomUtils.generateDateAfterSeconds(userRegTime, MIN_MINUTES, MAX_MINUTES);
                if (picUploadTime != null && viewTime.before(picUploadTime)) {
                    viewTime = RandomUtils.generateDateAfter(picUploadTime, MIN_MINUTES, MAX_MINUTES);
                }

                UserViewLogInfo log = new UserViewLogInfo();
                log.setViewId(IdUtils.snowflakeId().toString());
                log.setUserId(user.getUserId());
                log.setTargetType(PUserBehaviorTargetTypeEnum.USER_BEHAVIOR_TARGET_TYPE_0.getValue());
                log.setTargetId(pic.getPictureId());
                log.setTargetContent(pic.getName());
                log.setScore(0.0);
                log.setCategoryId(pic.getCategoryId());
                log.setSpaceId(pic.getSpaceId());
                List<String> tags = picTagMap.get(pic.getPictureId());
                if (tags != null && !tags.isEmpty()) {
                    log.setTags(String.join(COMMON_SEPARATOR, tags));
                }
                log.setTargetCover(pic.getThumbnailUrl());
                log.setCreateTime(viewTime);
                log.setHasStatistics(CommonHasStatisticsEnum.HAS_STATISTICS_0.getValue());
                log.setDeviceId(null);
                log.setBrowser("Chrome");
                log.setOs("Windows 11");
                log.setPlatform("Windows");
                log.setIpAddr(user.getLastLoginIp());
                log.setIpAddress(user.getIpAddress());

                currentBatch.add(log);
                if (currentBatch.size() >= BATCH_SIZE) {
                    batches.add(new ArrayList<>(currentBatch));
                    currentBatch.clear();
                }
                inserted++;
            }
        }
        if (!currentBatch.isEmpty()) {
            batches.add(currentBatch);
        }

        p("实际生成浏览记录：" + inserted + "，分 " + batches.size() + " 批，开始写入", startTime);
        saveBatchesParallel(batches, batch -> userViewLogInfoService.saveBatch(batch));
        p("浏览记录生成完成", startTime);
    }

    // ==================== 辅助方法 ====================

    /**
     * 按线性比例分配总数到各用户/空间。
     * 越靠前的单元分配的越多，总和保证恰好等于 target。
     * 公式：unit i 应得 = 2 × (N - i) / (N × (N + 1)) × target
     * 保证每个单元至少有 1 条。
     */
    private long[] computeLinearAllocation(long target, int count) {
        if (count <= 0) {
            return new long[0];
        }
        long[] result = new long[count];

        // 计算权重总和：权重序列为 [count, count-1, ..., 1]，总和 = count*(count+1)/2
        long totalWeight = 0;
        for (int i = 0; i < count; i++) {
            totalWeight += (count - i);
        }

        // 按权重比例初步分配，确保每个位置至少分配到 1
        long sum = 0;
        for (int i = 0; i < count; i++) {
            long allocation = Math.max(1, (long) Math.round((double) (count - i) / totalWeight * target));
            result[i] = allocation;
            sum += allocation;
        }

        // 调整四舍五入产生的误差，使总和精确等于 target
        long diff = target - sum;
        int idx = 0;
        while (diff != 0) {
            // 循环遍历数组，逐个 +1 或 -1，直到误差消除
            result[idx % count] += (diff > 0 ? 1 : -1);
            diff += (diff > 0 ? -1 : 1);
            idx++;
        }
        return result;
    }

    private void ensureTagsExist() {
        for (String tag : TAGS) {
            PictureTagInfo existing = pictureTagInfoService.getOne(
                    new LambdaQueryWrapper<PictureTagInfo>().eq(PictureTagInfo::getName, tag));
            if (existing != null) {
                continue;
            }
            Date createTime = RandomUtils.generateDate(2025, 2026);
            PictureTagInfo tagInfo = new PictureTagInfo();
            tagInfo.setTagId(IdUtils.snowflakeId().toString());
            tagInfo.setName(tag);
            tagInfo.setTagsStatus(PTagStatusEnum.TAG_STATUS_0.getValue());
            tagInfo.setTagDesc("");
            tagInfo.setUsageCount(0L);
            tagInfo.setLookCount(0L);
            tagInfo.setDownloadCount(0L);
            tagInfo.setUserId("1");
            tagInfo.setCreateTime(createTime);
            tagInfo.setUpdateTime(createTime);
            pictureTagInfoService.save(tagInfo);
        }
    }

    private Map<String, List<String>> buildPicTagMap(List<PictureInfo> picList) {
        List<String> picIds = picList.stream().map(PictureInfo::getPictureId).toList();
        List<PictureTagRelInfo> tagRels = pictureTagRelInfoService.list(
                new LambdaQueryWrapper<PictureTagRelInfo>()
                        .select(PictureTagRelInfo::getPictureId, PictureTagRelInfo::getTagName)
                        .in(PictureTagRelInfo::getPictureId, picIds));
        return tagRels.stream().collect(
                Collectors.groupingBy(PictureTagRelInfo::getPictureId,
                        Collectors.mapping(PictureTagRelInfo::getTagName, Collectors.toList())));
    }

    private <T> void saveBatchesParallel(List<List<T>> batches, Consumer<List<T>> saver) {
        ExecutorService executor = Executors.newFixedThreadPool(Math.min(batches.size(), 8));
        List<Future<?>> futures = new ArrayList<>();
        for (List<T> batch : batches) {
            futures.add(executor.submit(() -> saver.accept(batch)));
        }
        waitFutures(futures);
        executor.shutdown();
    }

    private void waitFutures(List<Future<?>> futures) {
        for (Future<?> f : futures) {
            try {
                f.get(30, TimeUnit.MINUTES);
            } catch (Exception e) {
                System.err.println("执行异常：" + e.getMessage());
            }
        }
    }

    private void p(String msg, long startTime) {
        long elapsedMillis = System.currentTimeMillis() - startTime;
        long minutes = elapsedMillis / 60000;
        long seconds = (elapsedMillis % 60000) / 1000;
        System.out.printf("[%d分%d秒] %s%n", minutes, seconds, msg);
    }

    private void p(String msg) {
        System.out.println(msg);
    }

    // ==================== 清理方法 ====================

    @Test
    public void deleteStatistics() {
        long start = System.currentTimeMillis();
        List<StatisticsInfo> list = statisticsInfoService.list(
                new LambdaQueryWrapper<StatisticsInfo>()
                        .notIn(StatisticsInfo::getType, List.of(
                                PStatisticsTypeEnum.STATISTICS_TYPE_1.getValue(),
                                PStatisticsTypeEnum.STATISTICS_TYPE_2.getValue(),
                                PStatisticsTypeEnum.STATISTICS_TYPE_3.getValue(),
                                PStatisticsTypeEnum.STATISTICS_TYPE_4.getValue(),
                                PStatisticsTypeEnum.STATISTICS_TYPE_5.getValue(),
                                PStatisticsTypeEnum.STATISTICS_TYPE_6.getValue())));
        List<String> ids = list.stream().map(StatisticsInfo::getStatisticsId).toList();
        statisticsInfoService.removeByIds(ids);
        p("删除统计完成", start);
    }

    @Test
    public void deletePicture() {
        long start = System.currentTimeMillis();
        deleteStatistics();
        int batch = BATCH_SIZE;
        p("开始删除图片", start);

        // ========== 1. 删除删除下载记录 ==========
        long totalDownloadDeleted = 0;
        int downLoopCount = 0;
        while (true) {
            boolean remove = pictureDownloadLogInfoService.remove(
                    new LambdaQueryWrapper<PictureDownloadLogInfo>()
                            .notIn(PictureDownloadLogInfo::getSpaceId, EXCLUDED_SPACE_IDS)
                            .notIn(PictureDownloadLogInfo::getUserId, EXCLUDED_USER_IDS)
                            .last("limit " + batch)
            );
            p("1 删除下载记录批次 " + downLoopCount + "：累计删 " + totalDownloadDeleted + " 条", start);
            if (!remove) {
                break;
            }
            totalDownloadDeleted += batch;
            downLoopCount++;
        }
        p("1 删除下载记录完成", start);
        //删除行为记录
        long totalBehaviorDeleted = 0;
        int behaviorLoopCount = 0;
        while (true) {
            boolean remove = userBehaviorInfoService.remove(
                    new LambdaQueryWrapper<UserBehaviorInfo>()
                            .notIn(UserBehaviorInfo::getSpaceId, EXCLUDED_SPACE_IDS)
                            .notIn(UserBehaviorInfo::getUserId, EXCLUDED_USER_IDS)
                            .last("limit " + batch)
            );
            p("2 删除行为记录批次 " + behaviorLoopCount + "：累计删 " + totalBehaviorDeleted + " 条", start);
            if (!remove) {
                break;
            }
            totalBehaviorDeleted += batch;
            behaviorLoopCount++;
        }
        p("2 删除行为记录完成", start);
        //删除浏览记录
        long totalViewDeleted = 0;
        int viewLoopCount = 0;
        while (true) {
            boolean remove = userViewLogInfoService.remove(
                    new LambdaQueryWrapper<UserViewLogInfo>()
                            .notIn(UserViewLogInfo::getSpaceId, EXCLUDED_SPACE_IDS)
                            .notIn(UserViewLogInfo::getUserId, EXCLUDED_USER_IDS)
                            .last("limit " + batch)
            );
            p("3 删除浏览记录批次 " + viewLoopCount + "：累计删 " + totalViewDeleted + " 条", start);
            if (!remove) {
                break;
            }
            totalViewDeleted += batch;
            viewLoopCount++;
        }
        //删除图片与标签关系
        long totalTagRelDeleted = 0;
        int tagRelLoopCount = 0;
        while (true) {
            boolean remove = pictureTagRelInfoService.remove(
                    new LambdaQueryWrapper<PictureTagRelInfo>()
                            .notIn(PictureTagRelInfo::getUserId, EXCLUDED_USER_IDS)
                            .last("limit " + batch)
            );
            p("4 删除图片与标签关系批次 " + tagRelLoopCount + "：累计删 " + totalTagRelDeleted + " 条", start);
            if (!remove) break;
            totalTagRelDeleted += batch;
            tagRelLoopCount++;
        }
        //删除图片
        long totalPicTagDeleted = 0;
        int picTagLoopCount = 0;
        while (true) {
            boolean remove = pictureInfoService.remove(
                    new LambdaQueryWrapper<PictureInfo>()
                            .notIn(PictureInfo::getSpaceId, EXCLUDED_SPACE_IDS)
                            .notIn(PictureInfo::getUserId, EXCLUDED_USER_IDS)
            );
            p("5 删除图片批次 " + picTagLoopCount + "：累计删 " + totalPicTagDeleted + " 条", start);
            if (!remove) {
                break;
            }
            totalPicTagDeleted += batch;
            picTagLoopCount++;
        }
        p("5 删除图片完成", start);
        //删除空间
        long totalSpaceTagDeleted = 0;
        int spaceTagLoopCount = 0;
        while (true) {
            boolean remove = spaceInfoService.remove(
                    new LambdaQueryWrapper<SpaceInfo>()
                            .notIn(SpaceInfo::getSpaceId, EXCLUDED_SPACE_IDS)
                            .notIn(SpaceInfo::getUserId, EXCLUDED_USER_IDS)
                            .last("limit " + batch)
            );
            p("6 删除空间批次 " + spaceTagLoopCount + "：累计删 " + totalSpaceTagDeleted + " 条", start);
            if (!remove) {
                break;
            }
            totalSpaceTagDeleted += batch;
            spaceTagLoopCount++;
        }
        p("6 删除空间完成", start);
        //删除搜索记录
        long totalSearchDeleted = 0;
        int searchLoopCount = 0;
        while (true) {
            boolean remove = searchLogInfoService.remove(
                    new LambdaQueryWrapper<SearchLogInfo>()
                            .notIn(SearchLogInfo::getUserId, EXCLUDED_USER_IDS)
                            .last("limit " + batch)
            );
            p("7 删除搜索记录批次 " + searchLoopCount + "：累计删 " + totalSearchDeleted + " 条", start);
            if (!remove) {
                break;
            }
            totalSearchDeleted += batch;
            searchLoopCount++;
        }
        //删除孤儿图片
        long totalOrphanPicDeleted = 0;
        int orphanPicLoopCount = 0;
        while (true) {
            boolean remove = pictureInfoService.remove(
                    new LambdaQueryWrapper<PictureInfo>()
                            .notIn(PictureInfo::getSpaceId, EXCLUDED_SPACE_IDS)
                            .notIn(PictureInfo::getUserId, EXCLUDED_USER_IDS)
                            .isNull(PictureInfo::getPictureId)
                            .last("limit " + batch)
            );
            p("8 删除孤儿图片批次 " + orphanPicLoopCount + "：累计删 " + totalOrphanPicDeleted + " 条", start);
            if (!remove) {
                break;
            }
            totalOrphanPicDeleted += batch;
            orphanPicLoopCount++;
        }
        p("8 删除孤儿图片完成", start);
        // ========== 2. 重算标签下载次数（先扫描下载日志统计，重算后删除） ==========
        Map<String, Long> tagDownloadCountMap = new HashMap<>();
        String lastDlId = "";
        int dlLoopCount = 0;
        long totalDlScanned = 0;
        while (true) {
            List<PictureDownloadLogInfo> dlBatchList = pictureDownloadLogInfoService.list(
                    new LambdaQueryWrapper<PictureDownloadLogInfo>()
                            .notIn(PictureDownloadLogInfo::getSpaceId, EXCLUDED_SPACE_IDS)
                            .gt(PictureDownloadLogInfo::getDownloadId, lastDlId)
                            .select(PictureDownloadLogInfo::getDownloadId, PictureDownloadLogInfo::getTags)
                            .orderByDesc(PictureDownloadLogInfo::getDownloadId)
                            .last("LIMIT " + batch));
            if (dlBatchList == null || dlBatchList.isEmpty()) {
                break;
            }
            lastDlId = dlBatchList.getLast().getDownloadId();
            totalDlScanned += dlBatchList.size();
            dlLoopCount++;
            for (PictureDownloadLogInfo log : dlBatchList) {
                if (StringUtils.isNotEmpty(log.getTags())) {
                    for (String tagName : log.getTags().split(COMMON_SEPARATOR)) {
                        if (StringUtils.isNotEmpty(tagName)) {
                            tagDownloadCountMap.merge(tagName, 1L, Long::sum);
                        }
                    }
                }
            }
            if (dlLoopCount % 10 == 0 || dlBatchList.size() < batch) {
                p("9 扫描下载日志批次 " + dlLoopCount + "：累计扫描 " + totalDlScanned + " 条", start);
            }
        }
        p("9 扫描下载日志完成，共 " + totalDlScanned + " 条，命中 " + tagDownloadCountMap.size() + " 个标签", start);

        List<PictureTagInfo> tagListReset = pictureTagInfoService.list();
        for (PictureTagInfo t : tagListReset) {
            Long usageCnt = pictureTagRelInfoService.count(
                    new LambdaQueryWrapper<PictureTagRelInfo>()
                            .eq(PictureTagRelInfo::getTagId, t.getTagId()));
            t.setUsageCount(usageCnt);
            t.setDownloadCount(tagDownloadCountMap.getOrDefault(t.getName(), 0L));
        }
        pictureTagInfoService.updateBatchById(tagListReset);
        p("10 重算标签统计完成，共 " + tagListReset.size() + " 个标签", start);

        // ========== 3. 重算分类使用数和下载次数 ==========
        List<PictureCategoryInfo> categoryList = pictureCategoryInfoService.list();
        for (PictureCategoryInfo c : categoryList) {
            Long usageCnt = pictureInfoService.count(
                    new LambdaQueryWrapper<PictureInfo>()
                            .eq(PictureInfo::getCategoryId, c.getCategoryId()));
            Long dlCnt = pictureDownloadLogInfoService.count(
                    new LambdaQueryWrapper<PictureDownloadLogInfo>()
                            .eq(PictureDownloadLogInfo::getCategoryId, c.getCategoryId()));
            c.setUsageCount(usageCnt);
            c.setDownloadCount(dlCnt);
        }
        pictureCategoryInfoService.updateBatchById(categoryList);
        p("11 重算分类统计完成，共 " + categoryList.size() + " 个分类", start);

        // ========== 4. 重置官方测试空间 ==========
        SpaceInfo testSpace = spaceInfoService.getById(TEST_SPACE_ID);
        if (testSpace != null) {
            testSpace.setTotalCount(0L);
            testSpace.setTotalSize(0L);
            spaceInfoService.updateById(testSpace);
        }
        p("12 重置测试空间完成", start);

        p("删除完成", start);
    }

}
