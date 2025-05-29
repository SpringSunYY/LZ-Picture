package com.lz.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.enums.CommonHasStatisticsEnum;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.picture.mapper.UserViewLogInfoMapper;
import com.lz.picture.model.domain.UserViewLogInfo;
import com.lz.picture.model.dto.userViewLogInfo.MyUserViewLogInfoQuery;
import com.lz.picture.model.dto.userViewLogInfo.UserViewLogInfoQuery;
import com.lz.picture.model.dto.userViewLogInfo.UserViewLogTargetInfoDto;
import com.lz.picture.model.vo.userViewLogInfo.UserViewLogInfoVo;
import com.lz.picture.service.IPictureTagInfoService;
import com.lz.picture.service.IPictureTagRelInfoService;
import com.lz.picture.service.IUserViewLogInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Resource
    private IPictureTagInfoService pictureTagInfoService;

    @Resource
    private IPictureTagRelInfoService pictureTagRelInfoService;

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
    public void recordUserViewLog(String userId, String targetType, double score, UserViewLogTargetInfoDto userViewLogTargetInfoDto, Date nowDate, DeviceInfo deviceInfo) {
        //判断今天有没有浏览记录，如果有就不插入了
        List<UserViewLogInfo> list = this.list(new LambdaQueryWrapper<UserViewLogInfo>()
                .eq(UserViewLogInfo::getUserId, userId)
                //时间精确到天
                .apply(StringUtils.isNotNull(nowDate),
                        "DATE_FORMAT(create_time, '%Y-%m-%d') = {0}",
                        DateUtils.parseDateToStr("yyyy-MM-dd", nowDate))
                .eq(UserViewLogInfo::getTargetId, userViewLogTargetInfoDto.getTargetId())
                .eq(UserViewLogInfo::getTargetType, targetType));
        UserViewLogInfo userViewLogInfo = new UserViewLogInfo();
        //如果存在
        if (StringUtils.isNotEmpty(list)) {
            userViewLogInfo = list.getFirst();
        } else {
            userViewLogInfo.setHasStatistics(CommonHasStatisticsEnum.HAS_STATISTICS_0.getValue());
        }
        //赋值
        userViewLogInfo.setUserId(userId);
        userViewLogInfo.setTargetType(targetType);
        userViewLogInfo.setTargetId(userViewLogTargetInfoDto.getTargetId());
        userViewLogInfo.setScore(score);
        userViewLogInfo.setCreateTime(nowDate);
        userViewLogInfo.setTargetContent(userViewLogTargetInfoDto.getTargetContent());
        userViewLogInfo.setCategoryId(userViewLogTargetInfoDto.getCategoryId());
        userViewLogInfo.setSpaceId(userViewLogTargetInfoDto.getSpaceId());
        userViewLogInfo.setTags(userViewLogTargetInfoDto.getTags());
        userViewLogInfo.setTargetCover(userViewLogTargetInfoDto.getTargetCover());
        userViewLogInfo.setDeviceId(deviceInfo.getDeviceId());
        userViewLogInfo.setBrowser(deviceInfo.getBrowser());
        userViewLogInfo.setOs(deviceInfo.getOs());
        userViewLogInfo.setPlatform(deviceInfo.getOs());
        userViewLogInfo.setIpAddr(deviceInfo.getIpAddr());
        userViewLogInfo.setIpAddress(deviceInfo.getIpAddress());
        this.saveOrUpdate(userViewLogInfo);
    }

    @Override
    public Page<UserViewLogInfo> selectMyUserViewLogInfoList(MyUserViewLogInfoQuery myUserViewLogInfoQuery) {
        // 提取基础参数
        Integer pageNum = myUserViewLogInfoQuery.getPageNum();
        Integer pageSize = myUserViewLogInfoQuery.getPageSize();
        Map<String, Object> params = myUserViewLogInfoQuery.getParams();

        // 提取 beginCreateTime 和 endCreateTime（安全获取）
        String beginCreateTime = Optional.ofNullable(params)
                .map(p -> p.get("beginCreateTime"))
                .map(Object::toString)
                .filter(StringUtils::isNotEmpty)
                .orElse(null);

        String endCreateTime = Optional.ofNullable(params)
                .map(p -> p.get("endCreateTime"))
                .map(Object::toString)
                .filter(StringUtils::isNotEmpty)
                .orElse(null);

        return this.page(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<UserViewLogInfo>()
                        .eq(StringUtils.isNotEmpty(myUserViewLogInfoQuery.getUserId()), UserViewLogInfo::getUserId, myUserViewLogInfoQuery.getUserId())
                        .eq(StringUtils.isNotEmpty(myUserViewLogInfoQuery.getTargetType()), UserViewLogInfo::getTargetType, myUserViewLogInfoQuery.getTargetType())
                        .like(StringUtils.isNotEmpty(myUserViewLogInfoQuery.getTargetContent()), UserViewLogInfo::getTargetContent, myUserViewLogInfoQuery.getTargetContent())
                        .apply(
                                StringUtils.isNotEmpty(beginCreateTime) && StringUtils.isNotEmpty(endCreateTime),
                                "create_time between {0} and {1}",
                                beginCreateTime, endCreateTime)
                        .orderBy(StringUtils.isNotEmpty(myUserViewLogInfoQuery.getIsAsc()),
                                myUserViewLogInfoQuery.getIsAsc().equals("asc"),
                                UserViewLogInfo::getCreateTime)
        );
    }

}
