package com.lz.picture;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.lz.picture.model.dto.pictureApiSearch.PictureApiSearchRequest;
import com.lz.picture.model.vo.pictureApiSearch.PictureApiSearchVo;
import com.lz.picture.service.IUserPictureApiSearchService;
import com.lz.picture.strategy.userPictureApiSearchStrategy.UserPictureApiSearchStrategyExecutor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;

/**
 * 图片搜索
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-12  23:06
 * @Version: 1.0
 */
@SpringBootTest
@Slf4j
public class PictureSearchTest {

    @Resource
    private IUserPictureApiSearchService pictureApiSearchService;

    @Resource
    private UserPictureApiSearchStrategyExecutor userPictureApiSearchStrategyExecutor;

    @Test
    public void getPictureSearchByKeywordBing() {
        // 要抓取的地址
        String fetchUrl = String.format("https://cn.bing.com/images/async?q=%s&mmasync=1", "懒羊羊4k壁纸");
        Document document = null;
        try {
            document = Jsoup.connect(fetchUrl).get();
        } catch (IOException e) {
            log.error("获取页面失败", e);
//            throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取页面失败");
        }
        Element div = document.getElementsByClass("dgControl").first();
        if (ObjUtil.isNull(div)) {
//            throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取元素失败");
        }
//        Elements imgElementList = div.select("img.mimg");
        Elements imgElementList = div.select("a.iusc");
        int uploadCount = 0;
        for (Element imgElement : imgElementList) {
//            String fileUrl = imgElement.attr("src");
            //获取m属性
            String m_attr = imgElement.attr("m");
            HashMap<String, String> mMap = JSONUtil.toBean(m_attr, HashMap.class);
            String fileUrl = mMap.get("murl");
            if (StrUtil.isBlank(fileUrl)) {
                log.info("当前链接为空，已跳过: {}", fileUrl);
                continue;
            }
            // 处理图片上传地址，防止出现转义问题
            int questionMarkIndex = fileUrl.indexOf("?");
            if (questionMarkIndex > -1) {
                fileUrl = fileUrl.substring(0, questionMarkIndex);
            }
            System.out.println("fileUrl = " + fileUrl);
            // 上传图片
        }
    }

    @Test
    public void bingTest() {
        PictureApiSearchRequest pictureApiSearchRequest = new PictureApiSearchRequest();
        pictureApiSearchRequest.setKeyword("懒羊羊高清壁纸");
        PictureApiSearchVo bing = pictureApiSearchService.keyword(pictureApiSearchRequest);
        System.out.println("bing = " + bing);
    }

    @Test
    public void strategyTest() {
        PictureApiSearchRequest pictureApiSearchRequest = new PictureApiSearchRequest();
        pictureApiSearchRequest.setApi("serpApi");
        pictureApiSearchRequest.setKeyword("懒羊羊4k高清壁纸");
        pictureApiSearchRequest.setCurrentPage(1);
        PictureApiSearchVo pictureApiSearchVo = userPictureApiSearchStrategyExecutor.executeGetUserPictureApiSearch(pictureApiSearchRequest);
        System.out.println("pictureApiSearchVo = " + pictureApiSearchVo);
    }
}
