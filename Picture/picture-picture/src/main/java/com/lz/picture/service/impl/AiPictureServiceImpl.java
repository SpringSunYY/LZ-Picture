package com.lz.picture.service.impl;

import com.lz.ai.api.aliyunai.AliYunAiApi;
import com.lz.ai.api.aliyunai.model.CreateOutPaintingTaskRequest;
import com.lz.ai.api.aliyunai.model.CreateOutPaintingTaskResponse;
import com.lz.ai.model.domain.GenerateLogInfo;
import com.lz.ai.model.enums.AiLogStatusEnum;
import com.lz.ai.model.vo.generateLogInfo.GenerateResponse;
import com.lz.ai.service.IGenerateLogInfoService;
import com.lz.ai.strategy.generate.domain.AiGenerateRequest;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.core.domain.model.LoginUserInfo;
import com.lz.common.manager.file.PictureDownloadManager;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.config.model.enmus.CTemplateTypeEnum;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.dto.pictureInfo.CreatePictureOutPaintingTaskRequest;
import com.lz.picture.model.enums.PPictureStatusEnum;
import com.lz.picture.model.enums.PSpaceTypeEnum;
import com.lz.picture.service.IAiPictureService;
import com.lz.picture.service.IPictureInfoService;
import com.lz.picture.service.ISpaceInfoService;
import com.lz.points.model.enums.PoPointsUsageLogTypeEnum;
import com.lz.points.model.enums.PoPointsUsageTypeEnum;
import com.lz.points.service.IPointsUsageLogInfoService;
import com.lz.user.manager.UserAsyncManager;
import com.lz.user.manager.factory.InformInfoAsyncFactory;
import com.lz.user.model.enums.UInformTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

import static com.lz.common.constant.config.TemplateInfoKeyConstants.AI_GENERATE_REFERENCE_IMAGE_REMIND;
import static com.lz.common.utils.DateUtils.YYYY_MM_DD_HH_MM_SS;

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
    private PictureDownloadManager pictureDownloadManager;

    @Resource
    private IGenerateLogInfoService generateLogInfoService;

    @Resource
    private IPointsUsageLogInfoService pointsUsageLogInfoService;


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
        if (spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_1.getValue())) {
            //如果是团队空间则判断用户是否是空间成员
            //TODO 后续有判断是否是团队空间 团队空间成员还是可以查看的
        } else {
            //不是团队空间，查看图片是否是自己的
            ThrowUtils.throwIf(!pictureInfo.getUserId().equals(loginUser.getUserId()), "图片不存在");
        }
        //构造请求参数
        CreateOutPaintingTaskRequest taskRequest = new CreateOutPaintingTaskRequest();
        CreateOutPaintingTaskRequest.Input input = new CreateOutPaintingTaskRequest.Input();
        input.setImageUrl(pictureDownloadManager.generateDownloadUrl(pictureInfo.getPictureUrl(), 5L));
        taskRequest.setInput(input);
        BeanUtils.copyProperties(createPictureOutPaintingTaskRequest, taskRequest);
        return aliYunAiApi.createOutPaintingTask(taskRequest);
    }

    @Override
    public List<GenerateResponse> userGenerate(AiGenerateRequest request) {
        //如果传入targetId
        if (StringUtils.isNotEmpty(request.getTargetId())) {
            PictureInfo pictureInfo = pictureInfoService.selectPictureInfoNormalByPictureId(request.getTargetId());
            ThrowUtils.throwIf(StringUtils.isNull(pictureInfo)
                            || (pictureInfo.getPictureStatus().equals(PPictureStatusEnum.PICTURE_STATUS_1.getValue()) && !pictureInfo.getUserId().equals(request.getUserId())),
                    "图片不存在");
            //获取到原图
            String url = pictureDownloadManager.generateDownloadUrl(pictureInfo.getPictureUrl(), 10L);
            request.setInputFile(url);
        }
        return generateLogInfoService.userGenerate(request);
    }

    @Override
    public GenerateLogInfo queryTask(String logId, String userId, String username) {
        GenerateLogInfo generateLogInfo = generateLogInfoService.queryTask(logId, userId, username);
        if (generateLogInfo.getLogStatus().equals(AiLogStatusEnum.LOG_STATUS_1.getValue()) && StringUtils.isNotEmpty(generateLogInfo.getTargetId())) {
            splitPoints(generateLogInfo);
        }
        return generateLogInfo;
    }

    //分成积分，如果成功给用户分成积分，失败则不处理
    private void splitPoints(GenerateLogInfo generateLogInfo) {
        //查询到是哪张图片
        PictureInfo pictureInfo = pictureInfoService.selectPictureInfoNormalByPictureId(generateLogInfo.getTargetId());
        if (StringUtils.isNull(pictureInfo) || StringUtils.isEmpty(pictureInfo.getUserId())) {
            return;
        }
        if (generateLogInfo.getUserId().equals(pictureInfo.getUserId())) {
            return;
        }
        //获取到积分使用情况
        Long pointsUsed = generateLogInfo.getPointsUsed();
        //解析价格*10，获取到积分
        BigDecimal points = generateLogInfo.getPriceUsed().multiply(BigDecimal.valueOf(100));
        //四舍五入转为 long
        points = points.setScale(0, RoundingMode.HALF_UP);
        //如果积分大于使用积分返回
        if (points.longValue() >= pointsUsed) {
            return;
        }
        //得到净利润积分
        long pointsProfit =  pointsUsed-points.longValue();
        //净利润积分除3，并且四舍五入为 long
        pointsProfit = pointsProfit / 3;
        DeviceInfo deviceInfo = new DeviceInfo();
        BeanUtils.copyProperties(generateLogInfo, deviceInfo);
        pointsUsageLogInfoService.updateAccountByPointsRechargeInfo(
                pictureInfo.getUserId(),
                generateLogInfo.getUserId(),
                PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_2.getValue(),
                PoPointsUsageTypeEnum.POINTS_USAGE_TYPE_0.getValue(),
                generateLogInfo.getLogId(),
                pointsProfit,
                deviceInfo
        );

        //发送消息提醒作者赚取积分
            /*
                {
                   "pictureName":"YY",
                   "points":"10000",
                   "createTime":"2025-05-26 10:11:12"
                }
             */
        HashMap<String, String> params = new HashMap<>();
        params.put("points", String.valueOf(pointsProfit));
        params.put("pictureName", pictureInfo.getName());
        params.put("createTime", DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM_SS, generateLogInfo.getCreateTime()));
        UserAsyncManager.me().execute(InformInfoAsyncFactory.sendInform(
                pictureInfo.getUserId(),
                AI_GENERATE_REFERENCE_IMAGE_REMIND,
                null,
                CTemplateTypeEnum.TEMPLATE_TYPE_3.getValue(),
                UInformTypeEnum.INFORM_TYPE_0.getValue(),
                params
        ));
    }
}
