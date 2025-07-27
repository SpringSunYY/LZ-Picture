package com.lz.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.CustomSort;
import com.lz.common.constant.HttpStatus;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.common.utils.ip.IpUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.config.model.enmus.CTemplateTypeEnum;
import com.lz.picture.mapper.UserReportInfoMapper;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.domain.UserReportInfo;
import com.lz.picture.model.dto.userReportInfo.UserReportInfoQuery;
import com.lz.picture.model.enums.*;
import com.lz.picture.model.vo.userReportInfo.UserReportInfoVo;
import com.lz.picture.service.IPictureInfoService;
import com.lz.picture.service.ISpaceInfoService;
import com.lz.picture.service.IUserReportInfoService;
import com.lz.user.manager.UserAsyncManager;
import com.lz.user.manager.factory.InformInfoAsyncFactory;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.model.enums.UInformTypeEnum;
import com.lz.user.service.IUserInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.lz.common.constant.config.TemplateInfoKeyConstants.PICTURE_REPORT_REMIND;
import static com.lz.common.constant.config.TemplateInfoKeyConstants.PICTURE_REPORT_SUCCESS;

/**
 * 用户举报信息Service业务层处理
 *
 * @author YY
 * @date 2025-06-17
 */
@Service
public class UserReportInfoServiceImpl extends ServiceImpl<UserReportInfoMapper, UserReportInfo> implements IUserReportInfoService {
    @Resource
    private UserReportInfoMapper userReportInfoMapper;

    @Resource
    private IPictureInfoService pictureInfoService;

    @Resource
    private ISpaceInfoService spaceInfoService;

    @Resource
    private IUserInfoService userInfoService;

    //region mybatis代码

    /**
     * 查询用户举报信息
     *
     * @param reportId 用户举报信息主键
     * @return 用户举报信息
     */
    @Override
    public UserReportInfo selectUserReportInfoByReportId(String reportId) {
        return userReportInfoMapper.selectUserReportInfoByReportId(reportId);
    }

    /**
     * 查询用户举报信息列表
     *
     * @param userReportInfo 用户举报信息
     * @return 用户举报信息
     */
    @CustomSort(sortFields = {"createTime", "reviewTime"}, sortMappingFields = {"create_time", "review_time"})
    @Override
    public List<UserReportInfo> selectUserReportInfoList(UserReportInfo userReportInfo) {
        return userReportInfoMapper.selectUserReportInfoList(userReportInfo);
    }

    /**
     * 新增用户举报信息
     *
     * @param userReportInfo 用户举报信息
     * @return 结果
     */
    @Override
    public int insertUserReportInfo(UserReportInfo userReportInfo) {
        userReportInfo.setCreateTime(DateUtils.getNowDate());
        return userReportInfoMapper.insertUserReportInfo(userReportInfo);
    }

    /**
     * 修改用户举报信息
     *
     * @param userReportInfo 用户举报信息
     * @return 结果
     */
    @Override
    public int updateUserReportInfo(UserReportInfo userReportInfo) {
        return userReportInfoMapper.updateUserReportInfo(userReportInfo);
    }

    /**
     * 批量删除用户举报信息
     *
     * @param reportIds 需要删除的用户举报信息主键
     * @return 结果
     */
    @Override
    public int deleteUserReportInfoByReportIds(String[] reportIds) {
        return userReportInfoMapper.deleteUserReportInfoByReportIds(reportIds);
    }

    /**
     * 删除用户举报信息信息
     *
     * @param reportId 用户举报信息主键
     * @return 结果
     */
    @Override
    public int deleteUserReportInfoByReportId(String reportId) {
        return userReportInfoMapper.deleteUserReportInfoByReportId(reportId);
    }

    //endregion
    @Override
    public QueryWrapper<UserReportInfo> getQueryWrapper(UserReportInfoQuery userReportInfoQuery) {
        QueryWrapper<UserReportInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = userReportInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String reportId = userReportInfoQuery.getReportId();
        queryWrapper.eq(StringUtils.isNotEmpty(reportId), "report_id", reportId);

        String userId = userReportInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        String reportType = userReportInfoQuery.getReportType();
        queryWrapper.eq(StringUtils.isNotEmpty(reportType), "report_type", reportType);

        String targetType = userReportInfoQuery.getTargetType();
        queryWrapper.eq(StringUtils.isNotEmpty(targetType), "target_type", targetType);

        String targetId = userReportInfoQuery.getTargetId();
        queryWrapper.eq(StringUtils.isNotEmpty(targetId), "target_id", targetId);

        String targetCover = userReportInfoQuery.getTargetCover();
        queryWrapper.eq(StringUtils.isNotEmpty(targetCover), "target_cover", targetCover);

        String reason = userReportInfoQuery.getReason();
        queryWrapper.eq(StringUtils.isNotEmpty(reason), "reason", reason);

        Date createTime = userReportInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        String reviewStatus = userReportInfoQuery.getReviewStatus();
        queryWrapper.eq(StringUtils.isNotNull(reviewStatus), "review_status", reviewStatus);

        String reviewMessage = userReportInfoQuery.getReviewMessage();
        queryWrapper.eq(StringUtils.isNotEmpty(reviewMessage), "review_message", reviewMessage);

        Long reviewUserId = userReportInfoQuery.getReviewUserId();
        queryWrapper.eq(StringUtils.isNotNull(reviewUserId), "review_user_id", reviewUserId);

        Date reviewTime = userReportInfoQuery.getReviewTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginReviewTime")) && StringUtils.isNotNull(params.get("endReviewTime")), "review_time", params.get("beginReviewTime"), params.get("endReviewTime"));

        String deviceId = userReportInfoQuery.getDeviceId();
        queryWrapper.like(StringUtils.isNotEmpty(deviceId), "device_id", deviceId);

        String browser = userReportInfoQuery.getBrowser();
        queryWrapper.like(StringUtils.isNotEmpty(browser), "browser", browser);

        String os = userReportInfoQuery.getOs();
        queryWrapper.eq(StringUtils.isNotEmpty(os), "os", os);

        String platform = userReportInfoQuery.getPlatform();
        queryWrapper.like(StringUtils.isNotEmpty(platform), "platform", platform);

        String ipAddr = userReportInfoQuery.getIpAddr();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddr), "ip_addr", ipAddr);

        String ipAddress = userReportInfoQuery.getIpAddress();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddress), "ip_address", ipAddress);

        return queryWrapper;
    }

    @Override
    public List<UserReportInfoVo> convertVoList(List<UserReportInfo> userReportInfoList) {
        if (StringUtils.isEmpty(userReportInfoList)) {
            return Collections.emptyList();
        }
        return userReportInfoList.stream().map(UserReportInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public int userInsertUserReportInfo(UserReportInfo userReportInfo) {
        //校验是否包含此类型
        ThrowUtils.throwIf(PReportTargetTypeEnum.getEnumByValue(userReportInfo.getTargetType()).isEmpty(), HttpStatus.BAD_REQUEST, "请选择正确的举报类型");
        ThrowUtils.throwIf(PReportTargetTypeEnum.getEnumByValue(userReportInfo.getReportType()).isEmpty(), HttpStatus.BAD_REQUEST, "请选择正确的举报类型");
        DeviceInfo deviceInfo = IpUtils.getDeviceInfo();
        BeanUtils.copyProperties(deviceInfo, userReportInfo);
        String targetId = "";
        String targetCover = "";
        String targetContent = "";
        //根据目标类型查询到对应的举目标
        switch (PReportTargetTypeEnum.getEnumByValue(userReportInfo.getTargetType()).get()) {
            case P_REPORT_TARGET_TYPE_0:
                PictureInfo pictureInfo = pictureInfoService.getById(userReportInfo.getTargetId());
                ThrowUtils.throwIf(StringUtils.isNull(pictureInfo)
                                || pictureInfo.getIsDelete().equals(CommonDeleteEnum.DELETED.getValue())
                                || pictureInfo.getPictureStatus().equals(PPictureStatusEnum.PICTURE_STATUS_1.getValue()),
                        "图片不存在");
                targetId = pictureInfo.getPictureId();
                //是否有域名，如果没有域名则是官方域名，无需添加域名头
                if (StringUtils.isEmpty(pictureInfo.getDnsUrl())) {
                    targetCover = pictureInfo.getThumbnailUrl();
                } else {
                    targetCover = pictureInfo.getDnsUrl() + pictureInfo.getThumbnailUrl();
                }
                targetContent = pictureInfo.getName();
                break;
            case P_REPORT_TARGET_TYPE_1:
                SpaceInfo spaceInfo = spaceInfoService.getById(userReportInfo.getTargetId());
                ThrowUtils.throwIf(StringUtils.isNull(spaceInfo)
                                || spaceInfo.getIsDelete().equals(CommonDeleteEnum.DELETED.getValue())
                                || spaceInfo.getSpaceStatus().equals(PSpaceStatusEnum.SPACE_STATUS_1.getValue()),
                        "空间不存在");
                targetId = spaceInfo.getSpaceId();
                targetCover = spaceInfo.getSpaceAvatar();
                targetContent = spaceInfo.getSpaceName();
                break;
            case P_REPORT_TARGET_TYPE_2:
                UserInfo userInfo = userInfoService.getById(userReportInfo.getTargetId());
                ThrowUtils.throwIf(StringUtils.isNull(userInfo)
                                || userInfo.getIsDelete().equals(CommonDeleteEnum.DELETED.getValue()),
                        "用户不存在");
                targetId = userInfo.getUserId();
                targetCover = userInfo.getAvatarUrl();
                targetContent = userInfo.getUserName();
                break;
            default:
                break;
        }
        userReportInfo.setReportId(IdUtils.snowflakeId().toString());
        userReportInfo.setTargetId(targetId);
        userReportInfo.setTargetCover(targetCover);
        userReportInfo.setTargetContent(targetContent);
        userReportInfo.setCreateTime(new Date());
        userReportInfo.setReviewStatus(PReportReviewStatusEnum.P_REPORT_REVIEW_STATUS_0.getValue());
        return userReportInfoMapper.insertUserReportInfo(userReportInfo);
    }

    @Override
    public int auditUserReportInfo(UserReportInfo userReportInfo) {
        //先查询举报信息
        UserReportInfo userReportInfoDb = userReportInfoMapper.selectUserReportInfoByReportId(userReportInfo.getReportId());
        //如果审核已经同意
        ThrowUtils.throwIf(userReportInfoDb.getReviewStatus().equals(PReportReviewStatusEnum.P_REPORT_REVIEW_STATUS_1.getValue()),
                "审核已经通过，无需再次审核");
        //当前只实现图片的审核
        ThrowUtils.throwIf(!userReportInfoDb.getReportType().equals(PReportTypeEnum.P_REPORT_TYPE_0.getValue()),
                "当前只可以审核图片");
        //如果是同意
        if (userReportInfo.getReviewStatus().equals(PReportReviewStatusEnum.P_REPORT_REVIEW_STATUS_1.getValue())) {
            //查询到图片信息
            PictureInfo pictureInfo = pictureInfoService.getById(userReportInfoDb.getTargetId());
            //更新图片为私有、拒绝
            pictureInfo.setPictureStatus(PPictureStatusEnum.PICTURE_STATUS_1.getValue());
            pictureInfoService.updatePictureInfo(pictureInfo);

            //为用户发送信息 举报者 作者
            /*
             * 1.发送给举报者
             * {
                      "userName":"YY",
                      "targetContent":"懒羊羊4k壁纸",
                      "targetType":"图片",
                      "reportType":"侵权",
                      "reviewMessage":"确实侵权，已处理"
                }
             */
            //举报者
            UserInfo userInfo = userInfoService.selectUserInfoByUserId(userReportInfoDb.getUserId());
            Map<String, String> params = new HashMap<>();
            params.put("userName", userInfo.getUserName());
            params.put("targetContent", userReportInfoDb.getTargetContent());
            Optional<PReportTargetTypeEnum> targetTypeEnumOptional = PReportTargetTypeEnum.getEnumByValue(userReportInfoDb.getTargetType());
            if (targetTypeEnumOptional.isPresent()) {
                params.put("targetType", targetTypeEnumOptional.get().getLabel());
            } else {
                params.put("targetType", "未知类型"); // 默认值
            }
            Optional<PReportTypeEnum> reportTypeEnumOptional = PReportTypeEnum.getEnumByValue(userReportInfo.getReportType());
            if (reportTypeEnumOptional.isPresent()) {
                params.put("reportType", reportTypeEnumOptional.get().getLabel());
            } else {
                params.put("reportType", "未知类型");
            }
            params.put("reviewMessage", userReportInfo.getReviewMessage());
            UserAsyncManager.me().execute(InformInfoAsyncFactory.sendInform(
                    userReportInfoDb.getUserId(),
                    PICTURE_REPORT_SUCCESS,
                    null,
                    CTemplateTypeEnum.TEMPLATE_TYPE_3.getValue(),
                    UInformTypeEnum.INFORM_TYPE_0.getValue(),
                    params));

            /*
             * 2.发送给作者
             * {
                      "userName":"YY",
                      "targetContent":"懒羊羊4k壁纸",
                      "targetType":"图片",
                      "reportType":"侵权",
                      "reviewMessage":"确实侵权，已处理"
                }
             */
            UserInfo authorUser = userInfoService.selectUserInfoByUserId(pictureInfo.getUserId());
            Map<String, String> authorParams = new HashMap<>();
            authorParams.put("userName", authorUser.getUserName());
            authorParams.put("targetContent", userReportInfoDb.getTargetContent());
            Optional<PReportTargetTypeEnum> authorTargetTypeEnumOptional = PReportTargetTypeEnum.getEnumByValue(userReportInfoDb.getTargetType());
            if (authorTargetTypeEnumOptional.isPresent()) {
                authorParams.put("targetType", authorTargetTypeEnumOptional.get().getLabel());
            } else {
                authorParams.put("targetType", "未知类型");
            }
            Optional<PReportTypeEnum> authorReportTypeEnumOptional = PReportTypeEnum.getEnumByValue(userReportInfo.getReportType());
            if (authorReportTypeEnumOptional.isPresent()) {
                authorParams.put("reportType", authorReportTypeEnumOptional.get().getLabel());
            } else {
                authorParams.put("reportType", "未知类型");
            }
            authorParams.put("reviewMessage", userReportInfo.getReviewMessage());
            UserAsyncManager.me().execute(InformInfoAsyncFactory.sendInform(
                    pictureInfo.getUserId(),
                    PICTURE_REPORT_REMIND,
                    null,
                    CTemplateTypeEnum.TEMPLATE_TYPE_3.getValue(),
                    UInformTypeEnum.INFORM_TYPE_0.getValue(),
                    authorParams));
        }
        userReportInfo.setReviewTime(new Date());
        return userReportInfoMapper.updateUserReportInfo(userReportInfo);
    }

}
