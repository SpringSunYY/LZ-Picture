package com.lz.picture.utils;

import com.lz.common.constant.Constants;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.utils.StringUtils;
import com.lz.picture.model.domain.SpaceMemberInfo;
import com.lz.picture.service.ISpaceMemberInfoService;
import com.lz.userauth.utils.UserInfoSecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用户空间权限类
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-07-01  17:01
 * @Version: 1.0
 */
@Component
public class SpaceAuthUtils {
    @Resource
    private ISpaceMemberInfoService spaceMemberInfoService;
    @Resource
    private RedisCache redisCache;

    public static final String SPACE_MEMBER_INFO_KEY = "picture:space:user:";

    // 令牌有效期（默认30分钟）
    @Value("${token.user.expireTime}")
    private int expireTime;

    /**
     * 获取空间成员权限
     *
     * @param userId 用户编号
     * @return Set<String>
     * @author: YY
     * @method: getSpaceMemberPerm
     * @date: 2025/7/1 17:14
     **/
    public Set<String> getSpaceMemberPerm(String userId) {
        String key = SPACE_MEMBER_INFO_KEY + userId;
        if (redisCache.hasKey(key)) {
            return redisCache.getCacheObject(key);
        }
        List<SpaceMemberInfo> spaceMemberInfoList = spaceMemberInfoService.selectSpaceMemberInfoByUserId(userId);
        Set<String> hashSet = new HashSet<>();
        for (SpaceMemberInfo spaceMemberInfo : spaceMemberInfoList) {
            hashSet.add(spaceMemberInfo.getUserId() + ":" + spaceMemberInfo.getSpaceId() + ":" + spaceMemberInfo.getRoleType());
        }
        redisCache.setCacheObject(key, hashSet, expireTime, TimeUnit.MINUTES);
        return hashSet;
    }

    public Set<String> getSpaceMemberPerm() {
        return getSpaceMemberPerm(UserInfoSecurityUtils.getUserId());
    }

    /**
     * 删除空间成员全权限
     *
     * @param userId
     * @return void
     * @author: YY
     * @method: deleteSpaceMemberPerm
     * @date: 2025/7/1 17:15
     **/
    public void deleteSpaceMemberPerm(String userId) {
        String key = SPACE_MEMBER_INFO_KEY + userId;
        redisCache.deleteObject(key);
    }

    /**
     * 检查空间成员权限
     *
     * @param userId     用户
     * @param permission 权限
     * @return boolean
     * @author: YY
     * @method: checkSpaceMemberPerm
     * @date: 2025/7/1 17:15
     **/
    public boolean checkSpaceMemberPerm(String userId, String permission) {
        if (StringUtils.isEmpty( permission)) {
            return false;
        }
        Set<String> spaceMemberPerm = getSpaceMemberPerm(userId);
        for (String perm : spaceMemberPerm) {
            if (perm.contains(permission)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkSpaceMemberPerm(String permission) {
        return checkSpaceMemberPerm(UserInfoSecurityUtils.getUserId(), permission);
    }

    /**
     * 是否包含任意权限
     * @param userId 用户编号
     * @param permission 权限，使用，分割
     * @return
     */
    public boolean checkSpaceMemberAnyPerm(String userId,String permission) {
        if (StringUtils.isEmpty( permission)) {
            return false;
        }
        String[] perms = permission.split(Constants.PERMISSION_DELIMETER);
        for (String perm : perms) {
            if (checkSpaceMemberPerm(userId, perm)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkSpaceMemberAnyPerm(String permission) {
        return checkSpaceMemberAnyPerm(UserInfoSecurityUtils.getUserId(), permission);
    }

    /**
     * 构建空间成员权限
     *
     * @param userId   用户编号
     * @param spaceId  空间编号
     * @param roleType 角色类型
     * @return String
     * @author: YY
     * @method: buildSpaceMemberPerm
     * @date: 2025/7/1 17:17
     **/
    public String buildSpaceMemberPerm(String userId, String spaceId, String roleType) {
        return userId + ":" + spaceId + ":" + roleType;
    }

    /**
     * 构建当前登录用户空间成员权限
     *
     * @param roleType 角色类型
     * @param spaceId  空间编号
     * @return String
     * @author: YY
     * @method: getSpaceMemberPerm
     * @date: 2025/7/1 17:17
     **/
    public String buildSpaceMemberPerm(String spaceId, String roleType) {
        return buildSpaceMemberPerm(UserInfoSecurityUtils.getUserId(), spaceId, roleType);
    }
}
