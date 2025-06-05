package com.lz.picture;

import com.lz.picture.mapper.PictureInfoMapper;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.dto.pictureInfo.PictureInfoRecommendRequest;
import com.lz.picture.model.vo.pictureInfo.UserPictureInfoVo;
import com.lz.picture.service.IPictureInfoService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

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
        PictureInfoRecommendRequest pictureInfoRecommendRequest = new PictureInfoRecommendRequest();
        pictureInfoRecommendRequest.setPictureId("1912539406024183808");
        pictureInfoRecommendRequest.setCurrentPage(1);
        pictureInfoRecommendRequest.setPageSize(10);
        pictureInfoRecommendRequest.setPictureStatus("0");
        pictureInfoRecommendRequest.setReviewStatus("1");
        List<UserPictureInfoVo> pictureInfoDetailRecommend = pictureInfoService.getPictureInfoDetailRecommend(pictureInfoRecommendRequest);
        System.out.println("pictureInfoDetailRecommend = " + pictureInfoDetailRecommend);

        List<PictureInfo> pictureInfoDetailRecommend1 = pictureInfoMapper.getPictureInfoDetailRecommend(pictureInfoRecommendRequest);
        System.out.println("pictureInfoDetailRecommend1 = " + pictureInfoDetailRecommend1);
    }
}
