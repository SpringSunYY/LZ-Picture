package com.lz.picture.aspectj;

import com.alibaba.fastjson2.JSON;
import com.lz.common.annotation.Log;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.core.domain.entity.SysUser;
import com.lz.common.core.domain.model.LoginUser;
import com.lz.common.enums.BusinessStatus;
import com.lz.common.enums.HttpMethod;
import com.lz.common.exception.ServiceException;
import com.lz.common.filter.PropertyPreExcludeFilter;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.ServletUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.common.utils.ip.IpUtils;
import com.lz.framework.manager.AsyncManager;
import com.lz.framework.manager.factory.AsyncFactory;
import com.lz.picture.annotation.SearchLog;
import com.lz.picture.model.domain.SearchLogInfo;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Map;

/**
 * 操作日志记录处理
 *
 * @author YY
 */
@Aspect
@Component
@Slf4j
public class SearchLogAspect {

    /**
     * 排除敏感属性字段
     */
    public static final String[] EXCLUDE_PROPERTIES = {"password", "oldPassword", "newPassword", "confirmPassword"};

    /**
     * 计算操作消耗时间
     */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<Long>("Cost Time");

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(searchLog)")
    public void boBefore(JoinPoint joinPoint, SearchLog searchLog) {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(searchLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, SearchLog searchLog, Object jsonResult) {
        handleLog(joinPoint, searchLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(searchLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, SearchLog searchLog, Exception e) {
        handleLog(joinPoint, searchLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, SearchLog searchLog, final Exception e, Object jsonResult) {
        try {
            SearchLogInfo searchLogInfo = new SearchLogInfo();
            // 设置消耗时间
            searchLogInfo.setSearchDuration(System.currentTimeMillis() - TIME_THREADLOCAL.get());
            //获取登录用户信息，如果没有会抛出登录异常
            String userId;
            try {
                userId = UserInfoSecurityUtils.getUserId();
            } catch (ServiceException ex) {
                // 用户未登录是正常业务场景，不记录错误日志
                userId = null; // 或者设置默认值
            }
            DeviceInfo deviceInfo = IpUtils.getDeviceInfo();
            BeanUtils.copyProperties(deviceInfo, searchLogInfo);
            searchLogInfo.setUserId(userId);

            // 保存数据库
            System.out.println("searchLogInfo = " + searchLogInfo);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        } finally {
            TIME_THREADLOCAL.remove();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param searchLog 日志
     * @param operLog   操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, SearchLog searchLog, SysOperLog operLog, Object jsonResult) throws Exception {

    }
}
