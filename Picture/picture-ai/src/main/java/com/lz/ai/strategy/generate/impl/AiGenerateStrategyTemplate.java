package com.lz.ai.strategy.generate.impl;

import com.lz.ai.model.domain.GenerateLogInfo;
import com.lz.ai.model.domain.ModelParamsInfo;
import com.lz.ai.strategy.generate.AiGenerateStrategyService;
import com.lz.ai.strategy.generate.domain.dto.GenerateLogInfoDto;
import com.lz.ai.strategy.generate.domain.params.Params;
import com.lz.ai.strategy.generate.domain.verify.Verify;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.points.model.enums.PoPointsUsageTypeEnum;
import com.lz.points.service.IPointsUsageLogInfoService;
import jakarta.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * AI生成模板方法模板
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-09  00:10
 * @Version: 1.0
 */
public class AiGenerateStrategyTemplate implements AiGenerateStrategyService {
    @Resource
    private IPointsUsageLogInfoService pointsUsageLogInfoService;
    /**
     * 用户生成
     *
     * @param info
     * @return String
     * @author: YY
     * @method: userGenerate
     * @date: 2025/8/9 00:25
     **/
    @Override
    public List<GenerateLogInfo> userGenerate(GenerateLogInfoDto info) {
        return new ArrayList<>();
    }

    @Override
    public GenerateLogInfo query(GenerateLogInfo query, ModelParamsInfo modelParamsInfo, String username) {
        return null;
    }

    public static void commonVerify(GenerateLogInfoDto info, Integer width, Integer minWidth, Integer maxWidth, Integer height, Integer minHeight, Integer maxHeight, Params jiMengParams, Verify jiMengVerify) {
        jiMengParams.setHeight(height);
        jiMengParams.setWidth(width);
        //如果宽高都在范围内
        if (width >= minWidth && width <= maxWidth && height >= minHeight && height <= maxHeight) {
            jiMengParams.setWidth(width);
            jiMengParams.setHeight(height);
        }
        //如果宽高不在范围内，
        //第二种情况：宽或者高在范围外,小于最小值，则取小的值最小值，等比例放大
        if (width < minWidth || height < minHeight) {
            if (width < height) {
                jiMengParams.setWidth(minWidth);
                jiMengParams.setHeight((int) (minWidth * height / width));
            } else {
                jiMengParams.setWidth((int) (minHeight * width / height));
                jiMengParams.setHeight(minHeight);
            }
        }
        //如果得到的宽高比例比如宽高相乘没有超过最大宽高相乘，这可以直接调用
        boolean isLimit = width * height > maxWidth * maxHeight;
        if (isLimit) {
            // 第一种情况：宽或者高在范围外，则取大的值的最大值，等比例缩小
            if (width > maxWidth || height > maxHeight) {
                if (width > height) {
                    jiMengParams.setWidth(maxWidth);
                    jiMengParams.setHeight((int) (maxWidth * height / width));
                } else {
                    jiMengParams.setWidth((int) (maxHeight * width / height));
                    jiMengParams.setHeight(maxHeight);
                }
            }
        }
        //如果提示词的长度超过最大长度
        if (info.getPrompt().length() > jiMengVerify.getPromptLength()) {
            jiMengParams.setPrompt(info.getPrompt().substring(0, jiMengVerify.getPromptLength()));
        } else {
            jiMengParams.setPrompt(info.getPrompt());
        }
    }

    /**
     * 执行积分操作
     * @param generateLogInfo
     * @param pointsUsed
     * @param logType
     */
    public void executePoints(GenerateLogInfo generateLogInfo,Long pointsUsed,String logType) {
        //添加文件日志
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceId(generateLogInfo.getDeviceId());
        deviceInfo.setIpAddr(generateLogInfo.getIpAddr());
        deviceInfo.setBrowser(generateLogInfo.getBrowser());
        deviceInfo.setOs(generateLogInfo.getOs());
        deviceInfo.setPlatform(generateLogInfo.getPlatform());
        pointsUsageLogInfoService.updateAccountByPointsRechargeInfo(generateLogInfo.getUserId(),
                null,
                logType,
                PoPointsUsageTypeEnum.POINTS_USAGE_TYPE_2.getValue(),
                generateLogInfo.getLogId(),
                pointsUsed,
                deviceInfo);
    }
}
