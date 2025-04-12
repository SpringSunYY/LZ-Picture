package com.lz.picture.aspectj;

import com.alibaba.fastjson2.JSON;
import com.lz.common.annotation.Log;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.core.domain.entity.SysUser;
import com.lz.common.core.domain.model.LoginUser;
import com.lz.common.enums.BusinessStatus;
import com.lz.common.enums.HttpMethod;
import com.lz.common.filter.PropertyPreExcludeFilter;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.ServletUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ip.IpUtils;
import com.lz.picture.annotation.UserViewLog;
import com.lz.picture.manager.PictureAsyncManager;
import com.lz.picture.manager.factory.PictureAsyncFactory;
import com.lz.system.model.domain.SysOperLog;
import com.lz.userauth.utils.UserInfoSecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 自定义图片浏览记录处理
 *
 * @author YY
 */
@Aspect
@Component
@Slf4j
public class UserViewLogAspect {
    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerUserViewLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, UserViewLog controllerUserViewLog, Object jsonResult) {
        handleLog(joinPoint, controllerUserViewLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerUserViewLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, UserViewLog controllerUserViewLog, Exception e) {
        log.error("自定义图片浏览记录异常信息：{}", e.getMessage());
    }

    protected void handleLog(final JoinPoint joinPoint, UserViewLog controllerUserViewLog, final Exception e, Object jsonResult) {
        String userId = UserInfoSecurityUtils.getUserId();
        String targetType = controllerUserViewLog.targetType();
        double score = controllerUserViewLog.score();
        System.out.println("score = " + score);
        System.out.println("userId = " + userId);
        System.out.println("targetType = " + targetType);
        jsonResult = JSON.toJSONString(jsonResult);
        System.out.println("jsonResult = " + jsonResult);
        DeviceInfo deviceInfo = IpUtils.getDeviceInfo();
        System.out.println("deviceInfo = " + JSON.toJSONString(deviceInfo));
        Date nowDate = DateUtils.getNowDate();
        PictureAsyncManager.me().execute(PictureAsyncFactory.recordUserViewLog(userId, targetType, score, deviceInfo, jsonResult, nowDate));
    }
}
