package com.lz.picture;

import com.lz.picture.model.dto.pictureRecommend.PictureRecommendRequest;
import com.lz.picture.model.dto.pictureRecommend.UserInterestModel;
import com.lz.picture.model.vo.pictureInfo.UserRecommendPictureInfoVo;
import com.lz.picture.service.IPictureInfoService;
import com.lz.picture.service.IPictureRecommendInfoService;
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

    @Resource
    private IPictureRecommendInfoService pictureRecommendInfoService;

    @Test
    public void getUserViewInterest() {
        UserInterestModel userViewInterest = pictureRecommendInfoService.getUserViewInterest("1", "0", 100, 1, 2, 0.95);
        System.out.println(userViewInterest);
    }

    @Test
    public void getUserBehaviorInterest() {
        UserInterestModel userBehaviorInterest = pictureRecommendInfoService.getUserBehaviorInterest("1", "0", 100, 1, 2, 0.95);
        System.out.println(userBehaviorInterest);
    }

    @Test
    public void getPictureDownloadInterest() {
        UserInterestModel pictureDownloadInterest = pictureRecommendInfoService.getPictureDownloadInterest("1", 100, 1, 2, 0.95);
        System.out.println(pictureDownloadInterest);
    }

    @Test
    public void getUserInterest() {
        UserInterestModel userInterest = pictureRecommendInfoService.getUserInterest("1");
//        userInterest.normalizeScores();
        System.out.println(userInterest);

        UserInterestModel interest = pictureRecommendInfoService.getUserInterrestModelByUserId("1321");
        System.err.println("interest = " + interest);
    }

    @Test
    public void getPictureInfoRecommend() {
        PictureRecommendRequest pictureRecommendRequest = new PictureRecommendRequest();
        pictureRecommendRequest.setUserId("1");
        pictureRecommendRequest.setCurrentPage(1);
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
