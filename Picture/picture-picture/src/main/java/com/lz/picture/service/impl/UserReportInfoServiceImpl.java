package com.lz.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.picture.mapper.UserReportInfoMapper;
import com.lz.picture.model.domain.UserReportInfo;
import com.lz.picture.model.dto.userReportInfo.UserReportInfoQuery;
import com.lz.picture.model.vo.userReportInfo.UserReportInfoVo;
import com.lz.picture.service.IUserReportInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

        Long targetId = userReportInfoQuery.getTargetId();
        queryWrapper.eq(StringUtils.isNotNull(targetId), "target_id", targetId);

        String targetCover = userReportInfoQuery.getTargetCover();
        queryWrapper.eq(StringUtils.isNotEmpty(targetCover), "target_cover", targetCover);

        String reason = userReportInfoQuery.getReason();
        queryWrapper.eq(StringUtils.isNotEmpty(reason), "reason", reason);

        Date createTime = userReportInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Long reviewStatus = userReportInfoQuery.getReviewStatus();
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

}
