package com.lz.points.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.CustomSort;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.ParamUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.points.mapper.PointsUsageLogInfoMapper;
import com.lz.points.model.domain.AccountInfo;
import com.lz.points.model.domain.PointsUsageLogInfo;
import com.lz.points.model.dto.pointsUsageLogInfo.PointsUsageLogInfoQuery;
import com.lz.points.model.dto.pointsUsageLogInfo.UserPointsUsageLogInfoQuery;
import com.lz.points.model.enums.PoAccountStatusEnum;
import com.lz.points.model.enums.PoPointsUsageLogTypeEnum;
import com.lz.points.model.vo.pointsUsageLogInfo.PointsUsageLogInfoVo;
import com.lz.points.service.IAccountInfoService;
import com.lz.points.service.IPointsUsageLogInfoService;
import com.lz.userauth.utils.UserInfoSecurityUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lz.common.constant.redis.PointsRedisConstants.*;

/**
 * 积分使用记录Service业务层处理
 *
 * @author YY
 * @date 2025-05-23
 */
@Slf4j
@Service
public class PointsUsageLogInfoServiceImpl extends ServiceImpl<PointsUsageLogInfoMapper, PointsUsageLogInfo> implements IPointsUsageLogInfoService {
    @Resource
    private PointsUsageLogInfoMapper pointsUsageLogInfoMapper;

    @Resource
    private IAccountInfoService accountInfoService;


    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisCache redisCache;

    //region mybatis代码

    /**
     * 查询积分使用记录
     *
     * @param logId 积分使用记录主键
     * @return 积分使用记录
     */
    @Override
    public PointsUsageLogInfo selectPointsUsageLogInfoByLogId(String logId) {
        return pointsUsageLogInfoMapper.selectPointsUsageLogInfoByLogId(logId);
    }

    /**
     * 查询积分使用记录列表
     *
     * @param pointsUsageLogInfo 积分使用记录
     * @return 积分使用记录
     */
    @CustomSort(sortFields = {
            "pointsBefore", "pointsUsed", "pointsAfter", "createTime", "updateTime"
    }, sortMappingFields = {
            "points_before", "points_used", "points_after", "create_time", "update_time"
    })
    @Override
    public List<PointsUsageLogInfo> selectPointsUsageLogInfoList(PointsUsageLogInfo pointsUsageLogInfo) {
        return pointsUsageLogInfoMapper.selectPointsUsageLogInfoList(pointsUsageLogInfo);
    }

    /**
     * 新增积分使用记录
     *
     * @param pointsUsageLogInfo 积分使用记录
     * @return 结果
     */
    @Override
    public int insertPointsUsageLogInfo(PointsUsageLogInfo pointsUsageLogInfo) {
        pointsUsageLogInfo.setCreateTime(DateUtils.getNowDate());
        return pointsUsageLogInfoMapper.insertPointsUsageLogInfo(pointsUsageLogInfo);
    }

    /**
     * 修改积分使用记录
     *
     * @param pointsUsageLogInfo 积分使用记录
     * @return 结果
     */
    @Override
    public int updatePointsUsageLogInfo(PointsUsageLogInfo pointsUsageLogInfo) {
        pointsUsageLogInfo.setUpdateTime(DateUtils.getNowDate());
        return pointsUsageLogInfoMapper.updatePointsUsageLogInfo(pointsUsageLogInfo);
    }

    /**
     * 批量删除积分使用记录
     *
     * @param logIds 需要删除的积分使用记录主键
     * @return 结果
     */
    @Override
    public int deletePointsUsageLogInfoByLogIds(String[] logIds) {
        return pointsUsageLogInfoMapper.deletePointsUsageLogInfoByLogIds(logIds);
    }

    /**
     * 删除积分使用记录信息
     *
     * @param logId 积分使用记录主键
     * @return 结果
     */
    @Override
    public int deletePointsUsageLogInfoByLogId(String logId) {
        return pointsUsageLogInfoMapper.deletePointsUsageLogInfoByLogId(logId);
    }

    //endregion
    @Override
    public QueryWrapper<PointsUsageLogInfo> getQueryWrapper(PointsUsageLogInfoQuery pointsUsageLogInfoQuery) {
        QueryWrapper<PointsUsageLogInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pointsUsageLogInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String logId = pointsUsageLogInfoQuery.getLogId();
        queryWrapper.eq(StringUtils.isNotEmpty(logId), "log_id", logId);

        String userId = pointsUsageLogInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        String giveUserId = pointsUsageLogInfoQuery.getGiveUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(giveUserId), "give_user_id", giveUserId);

        String logType = pointsUsageLogInfoQuery.getLogType();
        queryWrapper.eq(StringUtils.isNotEmpty(logType), "log_type", logType);

        String usageType = pointsUsageLogInfoQuery.getUsageType();
        queryWrapper.eq(StringUtils.isNotEmpty(usageType), "usage_type", usageType);

        String targetId = pointsUsageLogInfoQuery.getTargetId();
        queryWrapper.eq(StringUtils.isNotEmpty(targetId), "target_id", targetId);

        String deviceId = pointsUsageLogInfoQuery.getDeviceId();
        queryWrapper.eq(StringUtils.isNotEmpty(deviceId), "device_id", deviceId);

        String browser = pointsUsageLogInfoQuery.getBrowser();
        queryWrapper.like(StringUtils.isNotEmpty(browser), "browser", browser);

        String os = pointsUsageLogInfoQuery.getOs();
        queryWrapper.eq(StringUtils.isNotEmpty(os), "os", os);

        String platform = pointsUsageLogInfoQuery.getPlatform();
        queryWrapper.eq(StringUtils.isNotEmpty(platform), "platform", platform);

        String ipAddr = pointsUsageLogInfoQuery.getIpAddr();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddr), "ip_addr", ipAddr);

        String ipAddress = pointsUsageLogInfoQuery.getIpAddress();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddress), "ip_address", ipAddress);

        Date createTime = pointsUsageLogInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date updateTime = pointsUsageLogInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        String isDelete = pointsUsageLogInfoQuery.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete), "is_delete", isDelete);

        return queryWrapper;
    }

    @Override
    public List<PointsUsageLogInfoVo> convertVoList(List<PointsUsageLogInfo> pointsUsageLogInfoList) {
        if (StringUtils.isEmpty(pointsUsageLogInfoList)) {
            return Collections.emptyList();
        }
        return pointsUsageLogInfoList.stream().map(PointsUsageLogInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public Page<PointsUsageLogInfo> selectMyPointsUsageLogInfoList(UserPointsUsageLogInfoQuery userPointsUsageLogInfoQuery) {
        // 提取基础参数
        Integer pageNum = userPointsUsageLogInfoQuery.getPageNum();
        Integer pageSize = userPointsUsageLogInfoQuery.getPageSize();
        String beginCreateTime = ParamUtils.getSafeString(userPointsUsageLogInfoQuery, ParamUtils.BEGIN_CREATE_TIME);
        String endCreateTime = ParamUtils.getSafeString(userPointsUsageLogInfoQuery, ParamUtils.END_CREATE_TIME);

        return this.page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<PointsUsageLogInfo>()
                        .eq(StringUtils.isNotEmpty(userPointsUsageLogInfoQuery.getUserId()), PointsUsageLogInfo::getUserId, userPointsUsageLogInfoQuery.getUserId())
                        .eq(StringUtils.isNotEmpty(userPointsUsageLogInfoQuery.getLogType()), PointsUsageLogInfo::getLogType, userPointsUsageLogInfoQuery.getLogType())
                        .eq(StringUtils.isNotEmpty(userPointsUsageLogInfoQuery.getUsageType()), PointsUsageLogInfo::getUsageType, userPointsUsageLogInfoQuery.getUsageType())
                        .apply(StringUtils.isNotEmpty(beginCreateTime) && StringUtils.isNotEmpty(endCreateTime),
                                "create_time between {0} and {1}",
                                beginCreateTime, endCreateTime)
                        .orderBy(StringUtils.isNotEmpty(userPointsUsageLogInfoQuery.getIsAsc()),
                                userPointsUsageLogInfoQuery.getIsAsc().equals("asc"),
                                PointsUsageLogInfo::getCreateTime)
        );
    }

    @Override
    public int updateAccountByPointsRechargeInfo(String userId, String giveUserId, String logType, String usageType, String targetId, Long points, DeviceInfo deviceInfo) {
        //首先查到账户
        //查询到用户账户
        AccountInfo accountInfo = accountInfoService.selectAccountInfoByUserId(userId);
        Date nowDate = DateUtils.getNowDate();
        //判断用户账户是否存在
        if (StringUtils.isNull(accountInfo)) {
            //创建账户
            accountInfo = createAccount(userId, nowDate);
        }
        //积分使用记录
        PointsUsageLogInfo pointsUsageLogInfo = new PointsUsageLogInfo();
        pointsUsageLogInfo.setUserId(userId);
        pointsUsageLogInfo.setGiveUserId(giveUserId);
        pointsUsageLogInfo.setLogType(logType);
        pointsUsageLogInfo.setUsageType(usageType);
        pointsUsageLogInfo.setTargetId(targetId);
        pointsUsageLogInfo.setPointsBefore(accountInfo.getPointsBalance());
        pointsUsageLogInfo.setPointsUsed(points);
        //这里是＋ 需要减就传过来负数
        pointsUsageLogInfo.setPointsAfter(accountInfo.getPointsBalance() + points);
        accountInfo.setPointsBalance(accountInfo.getPointsBalance() + points);
        //如果传过来赠送用户编号，则表示是提成积分。并且是提成
        if (StringUtils.isNotEmpty(giveUserId)
                && PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_2.getValue().equals(logType)) {
            accountInfo.setPointsEarned(accountInfo.getPointsEarned() + points);
        }
        //如果是消费
        if (PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_1.getValue().equals(logType)) {
            //注意，此处消费传过来本来就要是负数，所以这里的积分需要转换为正数
            accountInfo.setPointsUsed(accountInfo.getPointsUsed() + (-points));
        }

        //基本信息
        BeanUtils.copyProperties(deviceInfo, pointsUsageLogInfo);
        pointsUsageLogInfo.setCreateTime(nowDate);
        pointsUsageLogInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
        //加锁，防止积分冲突
        RLock rLock = redissonClient.getLock(POINTS_USAGE_LOG_INFO_LOCK + userId);
        boolean locked = false;
        try {
            locked = rLock.tryLock(5, 30, TimeUnit.SECONDS);
            if (locked) {
                AccountInfo finalAccountInfo = accountInfo;
                transactionTemplate.execute(res -> {
                    //插入积分使用记录和更新用户账户
                    this.save(pointsUsageLogInfo);
                    return accountInfoService.saveOrUpdate(finalAccountInfo);
                });
            }
            redisCache.setCacheObject(POINTS_ACCOUNT_INFO + userId, accountInfo, POINTS_ACCOUNT_INFO_EXPIRE_TIME, TimeUnit.SECONDS);
            return 1;
        } catch (InterruptedException e) {
            log.error("获取锁失败", e);
            throw new ServiceException("积分充值失败！！！");
        } finally {
            if (locked) {
                rLock.unlock();
            }
        }
    }

    private static AccountInfo createAccount(String userId, Date nowDate) {
        AccountInfo accountInfo;
        accountInfo = new AccountInfo();
        accountInfo.setUserId(userId);
        //生成默认密码
        String password = RandomUtil.randomString(8);
        //随机为用户设置加密方式 md5、bcrypt 随机数，如果是1，则使用bcrypt加密，否则使用md5加密
        if (new Random().nextInt(2) == 1) {
            accountInfo.setPassword(UserInfoSecurityUtils.encodeEncryptPassword(password));
            accountInfo.setSalt("bcrypt");
        } else {
            accountInfo.setPassword(UserInfoSecurityUtils.encodeMd5Password(password));
            accountInfo.setSalt("md5");
        }
        //后续发送短信或者信息通知用户默认密码
        accountInfo.setPointsEarned(0L);
        accountInfo.setPointsUsed(0L);
        accountInfo.setRechargeAmount(new BigDecimal(BigInteger.ZERO));
        accountInfo.setPointsBalance(0L);
        accountInfo.setAccountStatus(PoAccountStatusEnum.ACCOUNT_STATUS_0.getValue());
        accountInfo.setCreateTime(nowDate);
        accountInfo.setUpdateTime(nowDate);
        accountInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
        return accountInfo;
    }

}
