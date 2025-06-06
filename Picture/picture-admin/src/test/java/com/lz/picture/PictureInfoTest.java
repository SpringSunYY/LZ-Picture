package com.lz.picture;

import com.lz.picture.mapper.PictureInfoMapper;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.dto.pictureInfo.PictureInfoDetailRecommendRequest;
import com.lz.picture.model.vo.pictureInfo.UserPictureInfoVo;
import com.lz.picture.service.IPictureInfoService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * TODO
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-06  02:11
 * @Version: 1.0
 */
@SpringBootTest
public class PictureInfoTest {
    @Resource
    private IPictureInfoService pictureInfoService;

    @Resource
    private PictureInfoMapper pictureInfoMapper;

    @Test
    public void getPictureInfoDetailRecommend() {
        PictureInfoDetailRecommendRequest pictureInfoDetailRecommendRequest = new PictureInfoDetailRecommendRequest();
        pictureInfoDetailRecommendRequest.setPictureId("1912539406024183808");
        pictureInfoDetailRecommendRequest.setCurrentPage(1);
        pictureInfoDetailRecommendRequest.setPageSize(10);
        pictureInfoDetailRecommendRequest.setPictureStatus("0");
        pictureInfoDetailRecommendRequest.setReviewStatus("1");
        List<UserPictureInfoVo> pictureInfoDetailRecommend = pictureInfoService.getPictureInfoDetailRecommend(pictureInfoDetailRecommendRequest);
        System.out.println("pictureInfoDetailRecommend = " + pictureInfoDetailRecommend);

        List<PictureInfo> pictureInfoDetailRecommend1 = pictureInfoMapper.getPictureInfoDetailRecommend(pictureInfoDetailRecommendRequest);
        System.out.println("pictureInfoDetailRecommend1 = " + pictureInfoDetailRecommend1);
    }
}
