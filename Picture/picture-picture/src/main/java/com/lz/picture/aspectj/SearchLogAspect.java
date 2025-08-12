package com.lz.picture.aspectj;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
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
import com.lz.picture.manager.PictureAsyncManager;
import com.lz.picture.manager.factory.SearchLogAsyncFactory;
import com.lz.picture.model.domain.SearchLogInfo;
import com.lz.picture.model.enums.PSearchStatusEnum;
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
            //解析请求参数和返回结果
            Map<?, ?> paramsMap = ServletUtils.getParamMap(ServletUtils.getRequest());
            Object name = paramsMap.get("name");
            if (StringUtils.isNull(name)) {
                return;
            }
            SearchLogInfo searchLogInfo = new SearchLogInfo();
            searchLogInfo.setKeyword(name.toString());
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
            searchLogInfo.setSearchType(searchLog.searchType());
            searchLogInfo.setReferSource(searchLog.referSource());
            //获取到返回结构的total
            if (StringUtils.isNotNull(jsonResult)) {
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(jsonResult));
                Long total = jsonObject.getLong("total");
                searchLogInfo.setResultCount(total);
                searchLogInfo.setSearchStatus(PSearchStatusEnum.SEARCH_STATUS_0.getValue());
            } else {
                searchLogInfo.setResultCount(0L);
                searchLogInfo.setSearchStatus(PSearchStatusEnum.SEARCH_STATUS_1.getValue());
                searchLogInfo.setFailReason(e.getMessage());
            }
            // 保存数据库
            PictureAsyncManager.me().execute(SearchLogAsyncFactory.recordSearchLog(searchLogInfo));
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
        } finally {
            TIME_THREADLOCAL.remove();
        }
    }
}
