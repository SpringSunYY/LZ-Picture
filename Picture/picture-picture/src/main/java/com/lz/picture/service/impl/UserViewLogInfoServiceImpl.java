package com.lz.picture.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.utils.StringUtils;

import com.lz.common.utils.DateUtils;
import com.lz.picture.model.vo.userViewLogInfo.UserViewLogTargetInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.picture.mapper.UserViewLogInfoMapper;
import com.lz.picture.model.domain.UserViewLogInfo;
import com.lz.picture.service.IUserViewLogInfoService;
import com.lz.picture.model.dto.userViewLogInfo.UserViewLogInfoQuery;
import com.lz.picture.model.vo.userViewLogInfo.UserViewLogInfoVo;

/**
 * 用户浏览记录Service业务层处理
 *
 * @author YY
 * @date 2025-04-12
 */
@Service
public class UserViewLogInfoServiceImpl extends ServiceImpl<UserViewLogInfoMapper, UserViewLogInfo> implements IUserViewLogInfoService {
    @Resource
    private UserViewLogInfoMapper userViewLogInfoMapper;

    //region mybatis代码

    /**
     * 查询用户浏览记录
     *
     * @param viewId 用户浏览记录主键
     * @return 用户浏览记录
     */
    @Override
    public UserViewLogInfo selectUserViewLogInfoByViewId(String viewId) {
        return userViewLogInfoMapper.selectUserViewLogInfoByViewId(viewId);
    }

    /**
     * 查询用户浏览记录列表
     *
     * @param userViewLogInfo 用户浏览记录
     * @return 用户浏览记录
     */
    @Override
    public List<UserViewLogInfo> selectUserViewLogInfoList(UserViewLogInfo userViewLogInfo) {
        return userViewLogInfoMapper.selectUserViewLogInfoList(userViewLogInfo);
    }

    /**
     * 新增用户浏览记录
     *
     * @param userViewLogInfo 用户浏览记录
     * @return 结果
     */
    @Override
    public int insertUserViewLogInfo(UserViewLogInfo userViewLogInfo) {
        userViewLogInfo.setCreateTime(DateUtils.getNowDate());
        return userViewLogInfoMapper.insertUserViewLogInfo(userViewLogInfo);
    }

    /**
     * 修改用户浏览记录
     *
     * @param userViewLogInfo 用户浏览记录
     * @return 结果
     */
    @Override
    public int updateUserViewLogInfo(UserViewLogInfo userViewLogInfo) {
        return userViewLogInfoMapper.updateUserViewLogInfo(userViewLogInfo);
    }

    /**
     * 批量删除用户浏览记录
     *
     * @param viewIds 需要删除的用户浏览记录主键
     * @return 结果
     */
    @Override
    public int deleteUserViewLogInfoByViewIds(String[] viewIds) {
        return userViewLogInfoMapper.deleteUserViewLogInfoByViewIds(viewIds);
    }

    /**
     * 删除用户浏览记录信息
     *
     * @param viewId 用户浏览记录主键
     * @return 结果
     */
    @Override
    public int deleteUserViewLogInfoByViewId(String viewId) {
        return userViewLogInfoMapper.deleteUserViewLogInfoByViewId(viewId);
    }

    //endregion
    @Override
    public QueryWrapper<UserViewLogInfo> getQueryWrapper(UserViewLogInfoQuery userViewLogInfoQuery) {
        QueryWrapper<UserViewLogInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = userViewLogInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String viewId = userViewLogInfoQuery.getViewId();
        queryWrapper.eq(StringUtils.isNotEmpty(viewId), "view_id", viewId);

        String userId = userViewLogInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        String targetType = userViewLogInfoQuery.getTargetType();
        queryWrapper.eq(StringUtils.isNotEmpty(targetType), "target_type", targetType);

        String targetId = userViewLogInfoQuery.getTargetId();
        queryWrapper.eq(StringUtils.isNotEmpty(targetId), "target_id", targetId);

        String targetContent = userViewLogInfoQuery.getTargetContent();
        queryWrapper.eq(StringUtils.isNotEmpty(targetContent), "target_content", targetContent);

        Double score = userViewLogInfoQuery.getScore();
        queryWrapper.eq(StringUtils.isNotNull(score), "score", score);

        String categoryId = userViewLogInfoQuery.getCategoryId();
        queryWrapper.eq(StringUtils.isNotEmpty(categoryId), "category_id", categoryId);

        String spaceId = userViewLogInfoQuery.getSpaceId();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceId), "space_id", spaceId);

        String tags = userViewLogInfoQuery.getTags();
        queryWrapper.like(StringUtils.isNotEmpty(tags), "tags", tags);

        Date createTime = userViewLogInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        String deviceId = userViewLogInfoQuery.getDeviceId();
        queryWrapper.eq(StringUtils.isNotEmpty(deviceId), "device_id", deviceId);

        String browser = userViewLogInfoQuery.getBrowser();
        queryWrapper.eq(StringUtils.isNotEmpty(browser), "browser", browser);

        String os = userViewLogInfoQuery.getOs();
        queryWrapper.eq(StringUtils.isNotEmpty(os), "os", os);

        String platform = userViewLogInfoQuery.getPlatform();
        queryWrapper.eq(StringUtils.isNotEmpty(platform), "platform", platform);

        String ipAddress = userViewLogInfoQuery.getIpAddress();
        queryWrapper.eq(StringUtils.isNotEmpty(ipAddress), "ip_address", ipAddress);

        return queryWrapper;
    }

    @Override
    public List<UserViewLogInfoVo> convertVoList(List<UserViewLogInfo> userViewLogInfoList) {
        if (StringUtils.isEmpty(userViewLogInfoList)) {
            return Collections.emptyList();
        }
        return userViewLogInfoList.stream().map(UserViewLogInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public void recordUserViewLog(String userId, String targetType, double score, UserViewLogTargetInfo userViewLogTargetInfo, Date nowDate, DeviceInfo deviceInfo) {
        //判断今天有没有浏览记录，如果有就不插入了
        List<UserViewLogInfo> list = this.list(new LambdaQueryWrapper<UserViewLogInfo>()
                .eq(UserViewLogInfo::getUserId, userId)
                //时间精确到天
                .apply(StringUtils.isNotNull(nowDate),
                        "DATE_FORMAT(create_time, '%Y-%m-%d') = {0}",
                        DateUtils.parseDateToStr("yyyy-MM-dd", nowDate))
                .eq(UserViewLogInfo::getTargetId, userViewLogTargetInfo.getTargetId())
                .eq(UserViewLogInfo::getTargetType, targetType));
        UserViewLogInfo userViewLogInfo = new UserViewLogInfo();
        //如果存在
        if (StringUtils.isNotEmpty(list)) {
            userViewLogInfo = list.get(0);

        }
        //赋值
        userViewLogInfo.setUserId(userId);
        userViewLogInfo.setTargetType(targetType);
        userViewLogInfo.setTargetId(userViewLogTargetInfo.getTargetId());
        userViewLogInfo.setScore(score);
        userViewLogInfo.setCreateTime(nowDate);
        userViewLogInfo.setTargetContent(userViewLogTargetInfo.getTargetContent());
        userViewLogInfo.setCategoryId(userViewLogTargetInfo.getCategoryId());
        userViewLogInfo.setSpaceId(userViewLogTargetInfo.getSpaceId());
        userViewLogInfo.setTags(userViewLogTargetInfo.getTags());
        userViewLogInfo.setTargetCover(userViewLogTargetInfo.getTargetCover());
        userViewLogInfo.setDeviceId(deviceInfo.getDeviceId());
        userViewLogInfo.setBrowser(deviceInfo.getBrowser());
        userViewLogInfo.setOs(deviceInfo.getOs());
        userViewLogInfo.setPlatform(deviceInfo.getOs());
        userViewLogInfo.setIpAddress(deviceInfo.getIpAddress());
        this.saveOrUpdate(userViewLogInfo);
    }

}
