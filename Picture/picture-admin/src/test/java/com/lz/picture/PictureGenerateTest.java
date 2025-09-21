package com.lz.picture;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.utils.RandomUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.picture.model.domain.SearchLogInfo;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.enums.PSearchReferSourceEnum;
import com.lz.picture.model.enums.PSearchStatusEnum;
import com.lz.picture.model.enums.PSearchTypeEnum;
import com.lz.picture.model.enums.PSpaceStatusEnum;
import com.lz.picture.service.ISearchLogInfoService;
import com.lz.picture.service.ISpaceInfoService;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.service.IUserInfoService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.map.LinkedMap;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;

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

    List<String> keywords = List.of(
            "YY", "Spring", "懒羊羊", "AI", "海绵宝宝", "蜡笔小新", "可爱壁纸", "搞笑表情包", "表情包", "可爱的壁纸", "哆啦A梦",
            "AI生图", "AI图片生成", "AI图片", "手机壁纸", "电脑高清壁纸", "壁纸", "AI氛围感", "懒羊羊头像", "懒羊羊表情包", "AI生成图片",
            "小黄鸭表情包", "小黄鸭头像", "城市夜景", "citywork", "广州", "可爱表情包", "手机壁纸", "搞笑壁纸", "Jay", "Jay Zhou", "Hello kitty",
            "wallhaven", "聊天背景", "室内特写", "脸部特写", "春节", "小黄鸭", "小心超人", "熊二", "好运连连", "心想事成", "恭喜发财",
            "卡通头像", "AI生成头像", "壁纸ins高级质感", "故事宿命感", "自然真实", "大师摄影", "真实人像摄影", "cosplay", "游戏cg", "夕阳", "日出",
            "蓝调", "猫咪", "Moebius", "头像插画", "极简主义", "复古风格", "艺术风格", "大师作品", "4K", "4k高清"
    );

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
}
