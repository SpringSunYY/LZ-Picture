package com.lz.userauth.utils;

import com.lz.userauth.model.domain.EncryptionPassword;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.StringUtils;

import java.util.Random;

/**
 * 用户密码工具类
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-21  23:37
 * @Version: 1.0
 */
public class PasswordUtils {
    /**
     * 校验密码格式是否正确
     *
     * @param password        密码
     * @param confirmPassword 确认密码
     * @return void
     * @author: YY
     * @method: checkPasswordFormate
     * @date: 2025/5/22 01:25
     **/
    public static void checkPasswordFormate(String password, String confirmPassword, int minLength, int maxLength) {
        //校验密码格式是否正确
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(confirmPassword)) {
            throw new ServiceException("密码不能为空！！！");
        }
        //校验长度
        if (password.length() < minLength || password.length() > maxLength) {
            throw new ServiceException(StringUtils.format("密码长度在{}~{}之间", minLength, maxLength));
        }
        //校验两次密码是否正确
        if (!password.equals(confirmPassword)) {
            throw new ServiceException("两次密码不一致");
        }
        //校验密码格式是否正确
        //至少8位且包含字母和数字，可使用符号但不能使用表情
        String regex = "^[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{}|;':\",./<>?`~ ]{" + minLength + "," + maxLength + "}$";
        if (!password.matches(regex)) {
            throw new ServiceException(StringUtils.format("密码格式不正确,密码长度在{}~{}之间,可以使用普通符号但不能表情",minLength,maxLength));
        }
    }

    /**
     * 加密密码随机使用加密算法
     *
     * @param password 密码
     * @return EncryptionPassword
     * @author: YY
     * @method: encryptPassword
     * @date: 2025/5/22 01:26
     **/
    public static EncryptionPassword encryptPassword(String password) {
        EncryptionPassword encryptionPassword = new EncryptionPassword();
        if (new Random().nextInt(2) == 1) {
            encryptionPassword.setPassword(UserInfoSecurityUtils.encodeEncryptPassword(password));
            encryptionPassword.setSalt("bcrypt");
        } else {
            encryptionPassword.setPassword(UserInfoSecurityUtils.encodeMd5Password(password));
            encryptionPassword.setSalt("md5");
        }
        return encryptionPassword;
    }

    /**
     * 检查密码是否正确 一样true，不一样false
     *
     * @param salt               加密算法
     * @param password           未加密密码
     * @param encryptionPassword 加密密码
     * @return boolean
     * @author: YY
     * @method: checkPassword
     * @date: 2025/5/22 01:25
     **/
    public static boolean checkPassword(String salt, String password, String encryptionPassword) {
        //根据加密方式对密码进行校验
        if ("bcrypt".equals(salt)) {
            return UserInfoSecurityUtils.matchesEncryptPassword(password, encryptionPassword);
        } else if ("md5".equals(salt)) {
            return UserInfoSecurityUtils.matchesMd5Password(password, encryptionPassword);
        } else {
            return false;
        }
    }
}
