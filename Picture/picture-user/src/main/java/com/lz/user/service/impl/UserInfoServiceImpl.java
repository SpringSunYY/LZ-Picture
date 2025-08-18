package com.lz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.CustomCacheable;
import com.lz.common.annotation.CustomSort;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.config.model.domain.MenuInfo;
import com.lz.config.service.IMenuInfoService;
import com.lz.user.manager.UserAsyncManager;
import com.lz.user.manager.factory.UserFileLogAsyncFactory;
import com.lz.user.mapper.UserInfoMapper;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.model.dto.userInfo.UserInfoQuery;
import com.lz.user.model.dto.userInfo.UserInfoUpdateAvatar;
import com.lz.user.model.dto.userInfo.UserPasswordUploadRequest;
import com.lz.user.model.vo.userInfo.MyUserInfoVo;
import com.lz.user.model.vo.userInfo.UserInfoVo;
import com.lz.user.service.IUserInfoService;
import com.lz.userauth.model.domain.EncryptionPassword;
import com.lz.userauth.utils.PasswordUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.common.constant.redis.UserRedisConstants.*;

/**
 * 用户信息Service业务层处理
 *
 * @author YY
 * @date 2025-03-17
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private IMenuInfoService menuInfoService;

    @Resource
    private RedisCache redisCache;

    //region mybatis代码

    /**
     * 查询用户信息
     *
     * @param userId 用户信息主键
     * @return 用户信息
     */
    @Override
    public UserInfo selectUserInfoByUserId(String userId) {
        return userInfoMapper.selectUserInfoByUserId(userId);
    }

    /**
     * 查询用户信息列表
     *
     * @param userInfo 用户信息
     * @return 用户信息
     */
    @CustomSort(sortFields = {"birthday", "lastLoginTime", "createTime", "updateTime"},
            sortMappingFields = {"birthday", "last_login_time", "create_time", "update_time"})
    @Override
    public List<UserInfo> selectUserInfoList(UserInfo userInfo) {
        return userInfoMapper.selectUserInfoList(userInfo);
    }

    /**
     * 新增用户信息
     *
     * @param userInfo 用户信息
     * @return 结果
     */
    @Override
    public int insertUserInfo(UserInfo userInfo) {
        userInfo.setCreateTime(DateUtils.getNowDate());
        return userInfoMapper.insertUserInfo(userInfo);
    }

    /**
     * 修改用户信息
     *
     * @param userInfo 用户信息
     * @return 结果
     */
    @Override
    public int updateUserInfo(UserInfo userInfo) {
        userInfo.setUpdateTime(DateUtils.getNowDate());
        //删除缓存
        redisCache.deleteObject(USER_MY_INFO + COMMON_SEPARATOR_CACHE + userInfo.getUserName());
        redisCache.deleteObject(USER_INFO + COMMON_SEPARATOR_CACHE + userInfo.getUserName());
        return userInfoMapper.updateUserInfo(userInfo);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户信息主键
     * @return 结果
     */
    @Override
    public int deleteUserInfoByUserIds(String[] userIds) {
        return userInfoMapper.deleteUserInfoByUserIds(userIds);
    }

    /**
     * 删除用户信息信息
     *
     * @param userId 用户信息主键
     * @return 结果
     */
    @Override
    public int deleteUserInfoByUserId(String userId) {
        return userInfoMapper.deleteUserInfoByUserId(userId);
    }

    //endregion
    @Override
    public QueryWrapper<UserInfo> getQueryWrapper(UserInfoQuery userInfoQuery) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = userInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String userId = userInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        String userName = userInfoQuery.getUserName();
        queryWrapper.like(StringUtils.isNotEmpty(userName), "user_name", userName);

        String phone = userInfoQuery.getPhone();
        queryWrapper.eq(StringUtils.isNotEmpty(phone), "phone", phone);

        String countryCode = userInfoQuery.getCountryCode();
        queryWrapper.eq(StringUtils.isNotEmpty(countryCode), "country_code", countryCode);

        String nickName = userInfoQuery.getNickName();
        queryWrapper.like(StringUtils.isNotEmpty(nickName), "nick_name", nickName);

        String status = userInfoQuery.getStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(status), "status", status);

        String salt = userInfoQuery.getSalt();
        queryWrapper.eq(StringUtils.isNotEmpty(salt), "salt", salt);

        String sex = userInfoQuery.getSex();
        queryWrapper.eq(StringUtils.isNotEmpty(sex), "sex", sex);

        Date birthday = userInfoQuery.getBirthday();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginBirthday")) && StringUtils.isNotNull(params.get("endBirthday")), "birthday", params.get("beginBirthday"), params.get("endBirthday"));

        String occupation = userInfoQuery.getOccupation();
        queryWrapper.like(StringUtils.isNotEmpty(occupation), "occupation", occupation);

        String preferredLanguageLocale = userInfoQuery.getPreferredLanguageLocale();
        queryWrapper.eq(StringUtils.isNotEmpty(preferredLanguageLocale), "preferred_language_locale", preferredLanguageLocale);

        String ipAddress = userInfoQuery.getIpAddress();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddress), "ip_address", ipAddress);

        Date lastLoginTime = userInfoQuery.getLastLoginTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginLastLoginTime")) && StringUtils.isNotNull(params.get("endLastLoginTime")), "last_login_time", params.get("beginLastLoginTime"), params.get("endLastLoginTime"));

        String lastLoginIp = userInfoQuery.getLastLoginIp();
        queryWrapper.like(StringUtils.isNotEmpty(lastLoginIp), "last_login_ip", lastLoginIp);

        Date createTime = userInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date updateTime = userInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        String isDelete = userInfoQuery.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete), "is_delete", isDelete);

        return queryWrapper;
    }

    @Override
    public List<UserInfoVo> convertVoList(List<UserInfo> userInfoList) {
        if (StringUtils.isEmpty(userInfoList)) {
            return Collections.emptyList();
        }
        return userInfoList.stream().map(UserInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public List<MenuInfo> getMenu(Set<String> permissions) {
        List<MenuInfo> menuInfoList = menuInfoService.getMenuInfo();
        //如果用户没有此权限则不显示此菜单
        if (StringUtils.isNotEmpty(permissions)) {
            return menuInfoList.stream().filter(menuInfo -> permissions.contains(menuInfo.getPerms())).collect(Collectors.toList());
        }
        return new LinkedList<>();
    }

    @CustomCacheable(keyPrefix = USER_MY_INFO, keyField = "username", expireTime = USER_MY_INFO_EXPIRE_TIME)
    @Override
    public MyUserInfoVo getMyUserInfoByUserName(String username) {
        UserInfo userInfo = this.getOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUserName, username));
        return MyUserInfoVo.objToVo(userInfo);
    }

    @Override
    public UserInfo userUpdateUserInfo(UserInfo userInfo) {
        //查询用户信息
        UserInfo userInfoDb = userInfoMapper.selectUserInfoByUserId(userInfo.getUserId());
        if (StringUtils.isNull(userInfoDb)) {
            return null;
        }
        userInfo.setUpdateTime(DateUtils.getNowDate());
        userInfoMapper.updateById(userInfo);
        userInfo.setUserName(userInfoDb.getUserName());
        //删除缓存
        redisCache.deleteObject(USER_MY_INFO + COMMON_SEPARATOR_CACHE + userInfoDb.getUserName());
        redisCache.deleteObject(USER_INFO + COMMON_SEPARATOR_CACHE + userInfoDb.getUserName());
        return userInfo;
    }

    @Override
    public int userUpdateUserInfoPassword(UserPasswordUploadRequest request) {
        //校验密码格式是否正确
        PasswordUtils.checkPasswordFormate(request.getPassword(), request.getConfirmPassword(), 8, 20);
        //先查询用户
        UserInfo userInfo = userInfoMapper.selectUserInfoByUserId(request.getUserId());
        if (StringUtils.isNull(userInfo)) {
            return 0;
        }
        //获取密码以及加密方式
        String password = userInfo.getPassword();
        String salt = userInfo.getSalt();
        if (!PasswordUtils.checkPassword(salt, request.getOldPassword(), password)) {
            return 0;
        }
        //密码校验成功 更新密码
        EncryptionPassword encrypted = PasswordUtils.encryptPassword(request.getPassword());
        userInfo.setPassword(encrypted.getPassword());
        userInfo.setSalt(encrypted.getSalt());
        return userInfoMapper.updateById(userInfo);
    }


    @Override
    public UserInfo userUpdateUserInfoAvatar(UserInfoUpdateAvatar userInfoUpdateAvatar) {
        //查询用户信息
        UserInfo userInfoDb = userInfoMapper.selectUserInfoByUserId(userInfoUpdateAvatar.getUserId());
        if (StringUtils.isNull(userInfoDb)) {
            return null;
        }
        //作为老数据
        UserInfo userInfoOld = new UserInfo();
        BeanUtils.copyProperties(userInfoDb, userInfoOld);
        userInfoDb.setAvatarUrl(userInfoUpdateAvatar.getAvatarUrl());
        userInfoMapper.updateById(userInfoDb);
        //删除缓存
        redisCache.deleteObject(USER_MY_INFO + COMMON_SEPARATOR_CACHE + userInfoDb.getUserName());
        redisCache.deleteObject(USER_INFO + COMMON_SEPARATOR_CACHE + userInfoDb.getUserName());
        //更新文件日志 因为老的数据赋值给userInfoOld，新数据重新赋值头像给userInfoDb
        UserAsyncManager.me().execute(UserFileLogAsyncFactory.updateUserInfoAvatarFileLog(userInfoOld, userInfoDb));
        return userInfoDb;
    }

    @CustomCacheable(keyPrefix = USER_INFO, keyField = "username", expireTime = USER_INFO_EXPIRE_TIME)
    @Override
    public UserInfo selectUserByUserName(String username) {
        return this.getOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUserName, username).eq(UserInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue()));
    }

}
