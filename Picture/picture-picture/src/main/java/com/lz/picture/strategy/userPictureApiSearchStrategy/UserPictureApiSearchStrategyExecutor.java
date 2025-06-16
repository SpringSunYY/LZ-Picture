package com.lz.picture.strategy.userPictureApiSearchStrategy;

import com.lz.common.constant.HttpStatus;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.picture.model.dto.pictureApiSearch.PictureApiSearchRequest;
import com.lz.picture.model.vo.pictureApiSearch.PictureApiSearchVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户图片api搜索策略执行器
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-16  15:06
 * @Version: 1.0
 */
@Service
public class UserPictureApiSearchStrategyExecutor {
    @Resource
    private List<UserPictureApiSearchStrategyService> userPictureApiSearchStrategyServiceList;

    public PictureApiSearchVo executeGetUserPictureApiSearch(PictureApiSearchRequest pictureApiSearchRequest) {
        ThrowUtils.throwIf(StringUtils.isNull(pictureApiSearchRequest) || StringUtils.isNull(pictureApiSearchRequest.getApi()), HttpStatus.BAD_REQUEST, "参数错误");
        for (UserPictureApiSearchStrategyService userPictureApiSearchStrategyService : userPictureApiSearchStrategyServiceList) {
            UserPictureApiSearchStrategyConfig annotation = userPictureApiSearchStrategyService.getClass().getAnnotation(UserPictureApiSearchStrategyConfig.class);
            if (annotation.api().equals(pictureApiSearchRequest.getApi())) {
                return userPictureApiSearchStrategyService.keywordSearch(pictureApiSearchRequest);
            }
        }
        //如果都没有就是不存在，不存在直接抛出异常处理
        throw new ServiceException("用户图片api搜索参数错误", HttpStatus.BAD_REQUEST);
    }
}
