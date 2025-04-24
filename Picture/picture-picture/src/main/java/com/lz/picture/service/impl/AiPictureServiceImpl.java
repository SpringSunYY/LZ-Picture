package com.lz.picture.service.impl;

import com.lz.ai.api.aliyunai.AliYunAiApi;
import com.lz.ai.api.aliyunai.model.CreateOutPaintingTaskRequest;
import com.lz.ai.api.aliyunai.model.CreateOutPaintingTaskResponse;
import com.lz.common.core.domain.model.LoginUserInfo;
import com.lz.common.manager.file.PictureUploadManager;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.dto.pictureInfo.CreatePictureOutPaintingTaskRequest;
import com.lz.picture.model.enums.PSpaceType;
import com.lz.picture.service.IAiPictureService;
import com.lz.picture.service.IPictureInfoService;
import com.lz.picture.service.ISpaceInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * AiPictureServiceImpl
 *
 * @author YY
 * @Project Picture
 * @package com.lz.picture.service.impl
 * @CreateTime: 2025-04-23  17:27
 * @Version: 1.0
 */
@Service
public class AiPictureServiceImpl implements IAiPictureService {
    @Resource
    private AliYunAiApi aliYunAiApi;

    @Resource
    private IPictureInfoService pictureInfoService;

    @Resource
    private ISpaceInfoService spaceInfoService;

    @Resource
    private PictureUploadManager pictureUploadManager;

    @Override
    public CreateOutPaintingTaskResponse createPictureOutPaintingTask(CreatePictureOutPaintingTaskRequest createPictureOutPaintingTaskRequest, LoginUserInfo loginUser) {
        //获取图片信息
        String pictureId = createPictureOutPaintingTaskRequest.getPictureId();
        PictureInfo pictureInfo = pictureInfoService.selectPictureInfoByPictureId(pictureId);
        ThrowUtils.throwIf(StringUtils.isNull(pictureInfo), "图片不存在");
        //根据图片信息获取空间信息
        SpaceInfo spaceInfo = spaceInfoService.selectSpaceInfoBySpaceId(pictureInfo.getSpaceId());
        ThrowUtils.throwIf(StringUtils.isNull(spaceInfo), "空间不存在");
        //查看空间是否是团队空间
        if (spaceInfo.getSpaceType().equals(PSpaceType.SPACE_TYPE_1.getValue())) {
            //如果是团队空间则判断用户是否是空间成员
            //TODO 后续有判断是否是团队空间 团队空间成员还是可以查看的
        } else {
            //不是团队空间，查看图片是否是自己的
            ThrowUtils.throwIf(!pictureInfo.getUserId().equals(loginUser.getUserId()), "图片不存在");
        }
        //构造请求参数
        CreateOutPaintingTaskRequest taskRequest = new CreateOutPaintingTaskRequest();
        CreateOutPaintingTaskRequest.Input input = new CreateOutPaintingTaskRequest.Input();
        input.setImageUrl(pictureUploadManager.generateDownloadUrl(pictureInfo.getPictureUrl(), 5L));
        taskRequest.setInput(input);
        BeanUtils.copyProperties(createPictureOutPaintingTaskRequest, taskRequest);
        return aliYunAiApi.createOutPaintingTask(taskRequest);
    }
}
