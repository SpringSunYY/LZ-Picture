package com.lz.picture;

import com.lz.picture.model.dto.pictureRecommend.UserInterestModel;
import com.lz.picture.service.IPictureRecommendService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
    private IPictureRecommendService pictureRecommendService;

    @Test
    public void getUserViewInterest(){
        UserInterestModel userViewInterest = pictureRecommendService.getUserViewInterest("1", "0", 100, 1, 2, 0.95);
        System.out.println(userViewInterest);
    }

    @Test
    public void getUserBehaviorInterest(){
        UserInterestModel userBehaviorInterest = pictureRecommendService.getUserBehaviorInterest("1", "0", 100, 1, 2, 0.95);
        System.out.println(userBehaviorInterest);
    }

    @Test
    public void getPictureDownloadInterest(){
        UserInterestModel pictureDownloadInterest = pictureRecommendService.getPictureDownloadInterest("1", 100, 1, 2, 0.95);
        System.out.println(pictureDownloadInterest);
    }

    @Test
    public void getUserInterest(){
        UserInterestModel userInterest = pictureRecommendService.getUserInterest("1");
        System.out.println(userInterest);
    }
}
