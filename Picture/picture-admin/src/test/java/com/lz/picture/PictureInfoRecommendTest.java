package com.lz.picture;

import com.lz.picture.model.dto.pictureRecommend.PictureRecommendRequest;
import com.lz.picture.model.dto.pictureRecommend.UserInterestModel;
import com.lz.picture.model.vo.pictureInfo.UserRecommendPictureInfoVo;
import com.lz.picture.service.IPictureInfoService;
import com.lz.picture.service.IPictureRecommendService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * TODO
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-06  22:44
 * @Version: 1.0
 */
@SpringBootTest
public class PictureInfoRecommendTest {
    @Resource
    private IPictureInfoService pictureInfoService;
    @Resource
    private IPictureRecommendService pictureRecommendService;

    @Test
    public void getUserViewInterest() {
        UserInterestModel userViewInterest = pictureRecommendService.getUserViewInterest("1", "0", 100, 1, 2, 0.95);
        System.out.println(userViewInterest);
    }

    @Test
    public void getUserBehaviorInterest() {
        UserInterestModel userBehaviorInterest = pictureRecommendService.getUserBehaviorInterest("1", "0", 100, 1, 2, 0.95);
        System.out.println(userBehaviorInterest);
    }

    @Test
    public void getPictureDownloadInterest() {
        UserInterestModel pictureDownloadInterest = pictureRecommendService.getPictureDownloadInterest("1", 100, 1, 2, 0.95);
        System.out.println(pictureDownloadInterest);
    }

    @Test
    public void getUserInterest() {
        UserInterestModel userInterest = pictureRecommendService.getUserInterest("1");
        userInterest.normalizeScores();
        System.out.println(userInterest);
    }

    @Test
    public void getPictureInfoRecommend() {
        PictureRecommendRequest pictureRecommendRequest = new PictureRecommendRequest();
        pictureRecommendRequest.setUserId("1");
        pictureRecommendRequest.setCurrentPage(3);
        pictureRecommendRequest.setPageSize(30);
        List<UserRecommendPictureInfoVo> pictureInfoRecommend = pictureRecommendService.getPictureInfoRecommend(pictureRecommendRequest);
        System.out.println("pictureInfoRecommend = " + pictureInfoRecommend.size());
        for (UserRecommendPictureInfoVo userRecommendPictureInfoVo : pictureInfoRecommend) {
            System.err.println("pictureId = " + userRecommendPictureInfoVo.getPictureId());
            System.out.println("name = " + userRecommendPictureInfoVo.getName());
            System.out.println("category = " + userRecommendPictureInfoVo.getCategoryId());
        }
    }

    @Test
    public void getPictureInfoHotRecommend() {
        PictureRecommendRequest pictureRecommendRequest = new PictureRecommendRequest();
        pictureRecommendRequest.setCurrentPage(1);
        pictureRecommendRequest.setPageSize(100);
        List<UserRecommendPictureInfoVo> recommentHotPictureInfoList = pictureInfoService.getRecommentHotPictureInfoList(pictureRecommendRequest);
        System.out.println(recommentHotPictureInfoList.size());
    }
}
