package com.lz.picture.service;

import com.lz.ai.api.aliyunai.model.CreateOutPaintingTaskResponse;
import com.lz.common.core.domain.model.LoginUserInfo;
import com.lz.picture.model.dto.pictureInfo.CreatePictureOutPaintingTaskRequest;

/**
 * AI编辑图片
 * Project: Picture
 * Package: com.lz.picture.service
 * Author: YY
 * CreateTime: 2025-04-23  17:17
 * Description: IAiPictureService
 * Version: 1.0
 */
public interface IAiPictureService {
    public CreateOutPaintingTaskResponse createPictureOutPaintingTask(CreatePictureOutPaintingTaskRequest createPictureOutPaintingTaskRequest, LoginUserInfo loginUser);
}
