package com.lz.picture.strategy.userPictureApiSearchStrategy.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.lz.common.exception.ServiceException;
import com.lz.picture.model.dto.pictureApiSearch.PictureApiSearchRequest;
import com.lz.picture.model.vo.pictureApiSearch.PictureApiSearchVo;
import com.lz.picture.strategy.userPictureApiSearchStrategy.UserPictureApiSearchStrategyConfig;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * bing api搜索策略实现类
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-16  15:48
 * @Version: 1.0
 */
@Slf4j
@UserPictureApiSearchStrategyConfig(api = "bing")
public class BingApiSearchStrategyServiceImpl extends UserPictureApiSearchStrategyTemplate {
    @Override
    public PictureApiSearchVo keywordSearch(PictureApiSearchRequest pictureApiSearchRequest) {
        // 参数校验
        verifyParams(pictureApiSearchRequest);
        int first = (pictureApiSearchRequest.getCurrentPage() - 1) * 35;
        // 要抓取的地址
        String fetchUrl = String.format("https://cn.bing.com/images/async?q=%s&mmasync=1&first=%s", pictureApiSearchRequest.getKeyword(), first);
        Document document = null;
        try {
            document = Jsoup.connect(fetchUrl).get();
        } catch (IOException e) {
            log.error("获取页面失败", e);
            throw new ServiceException("获取信息失败，请稍后再试！！！");
        }
        Element div = document.getElementsByClass("dgControl").first();
        if (ObjUtil.isNull(div)) {
            throw new ServiceException("获取信息失败，请稍后再试！！！");
        }
        PictureApiSearchVo pictureApiSearchVo = new PictureApiSearchVo();
        pictureApiSearchVo.setUserId(pictureApiSearchRequest.getUserId());
        pictureApiSearchVo.setApi(pictureApiSearchRequest.getApi());
        pictureApiSearchVo.setModel(pictureApiSearchRequest.getModel());
        pictureApiSearchVo.setKeyword(pictureApiSearchRequest.getKeyword());
        List<String> urls = new ArrayList<>();
        int count = 0;
        Elements imgElementList = div.select("a.iusc");
        for (Element imgElement : imgElementList) {
//            String fileUrl = imgElement.attr("src");
//            System.out.println("imgElement = " + imgElement);
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
            count++;
//            System.out.println("fileUrl = " + fileUrl);
            urls.add(fileUrl);
        }
        pictureApiSearchVo.setMaxCount(35);
        pictureApiSearchVo.setCount(count);
        pictureApiSearchVo.setUrls(urls);
        return pictureApiSearchVo;
    }
}
