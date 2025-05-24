package com.lz.points.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.common.utils.ip.IpUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.points.config.AlipayPaymentConfig;
import com.lz.points.constant.PayConstants;
import com.lz.points.manager.AlipayManager;
import com.lz.points.manager.model.AlipayCallbackRequest;
import com.lz.points.manager.model.AlipayPcPaymentRequest;
import com.lz.points.manager.model.AlipayPcPaymentResponse;
import com.lz.points.model.domain.*;
import com.lz.points.model.dto.pay.PayRequest;
import com.lz.points.model.enums.*;
import com.lz.points.model.vo.pay.AlipayPcPaymentVo;
import com.lz.points.model.vo.paymentOrderInfo.UserPaymentOrderInfoVo;
import com.lz.points.service.*;
import com.lz.userauth.utils.UserInfoSecurityUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.common.constant.redis.PointsRedisConstants.POINTS_ORDER_DETAIL;
import static com.lz.common.constant.redis.PointsRedisConstants.POINTS_ORDER_LOCK;
import static com.lz.points.constant.PayConstants.ALIPAY_WEB;
import static com.lz.points.constant.PayConstants.WAIT_BUYER_PAY;

/**
 * 支付
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-15  09:39
 * @Version: 1.0
 */
@Slf4j
@Service
public class PayServiceImpl implements IPayService {
    @Resource
    private IPointsRechargePackageInfoService pointsRechargePackageInfoService;

    @Resource
    private AlipayManager alipayManager;

    @Resource
    private AlipayPaymentConfig alipayPaymentConfig;

    @Resource
    private IPaymentOrderInfoService paymentOrderInfoService;

    @Resource
    private IPointsRechargeInfoService pointsRechargeInfoService;

    @Resource
    private IAccountInfoService accountInfoService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private IErrorLogInfoService errorLogInfoService;

    @Resource
    private IPointsUsageLogInfoService pointsUsageLogInfoService;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public AlipayPcPaymentVo alipayWeb(PayRequest payRequest) {
        //首先查询套餐详情
        PointsRechargePackageInfo pointsRechargePackageInfo = pointsRechargePackageInfoService.selectPointsRechargePackageInfoByPackageId(payRequest.getPackageId());
        ThrowUtils.throwIf(StringUtils.isNull(pointsRechargePackageInfo), "套餐不存在!!!");
        //状态是否正常
        ThrowUtils.throwIf(!pointsRechargePackageInfo.getPackageStatus().equals(PoPackageStatusEnum.PACKAGE_STATUS_1.getValue()), "套餐未开始!!!");
        //如果套餐不是长期套餐，判断是否在时间范围内
        if (!pointsRechargePackageInfo.getIsLongTerm().equals(PoPackageIsLongTermEnum.PACKAGE_IS_LONG_TERM_0.getValue())) {
            ThrowUtils.throwIf(pointsRechargePackageInfo.getStartTime().getTime() > System.currentTimeMillis(), "套餐未开始!!!");
            ThrowUtils.throwIf(pointsRechargePackageInfo.getEndTime().getTime() < System.currentTimeMillis(), "套餐已结束!!!");
        }
        AlipayPcPaymentRequest alipayPcPaymentRequest = new AlipayPcPaymentRequest();
        BigDecimal price = pointsRechargePackageInfo.getPrice();
        alipayPcPaymentRequest.setTotalAmount(price);
        payRequest.setTotalAmount(price);
        //自定义订单号
        String outTradeNo = IdUtils.snowflakeId().toString();
        payRequest.setOutTradeNo(outTradeNo);
        alipayPcPaymentRequest.setOutTradeNo(outTradeNo);
        alipayPcPaymentRequest.setSubject(pointsRechargePackageInfo.getPackageName());
        alipayPcPaymentRequest.setBuyerId(payRequest.getUserId());
        alipayPcPaymentRequest.setTimeoutExpress(alipayPaymentConfig.getTimeoutExpress());
        alipayPcPaymentRequest.setProductCode(alipayPaymentConfig.getProductCode());

        AlipayPcPaymentResponse alipayPcPaymentResponse = alipayManager.pcPay(alipayPcPaymentRequest);
        AlipayPcPaymentVo paymentVo = new AlipayPcPaymentVo();
        BeanUtils.copyProperties(alipayPcPaymentResponse, paymentVo);
        //创建订单
        createOrder(payRequest, pointsRechargePackageInfo);
        return paymentVo;
    }

    private void createOrder(PayRequest request, PointsRechargePackageInfo packageInfo) {
        //支付信息
        PaymentOrderInfo orderInfo = new PaymentOrderInfo();
        orderInfo.setOrderId(request.getOutTradeNo());
        orderInfo.setUserId(request.getUserId());
        orderInfo.setOrderStatus(PoOrderStatusEnum.ORDER_STATUS_0.getValue());
        orderInfo.setPaymentType(PoPaymentTypeEnum.PAYMENT_TYPE_0.getValue());
        orderInfo.setTotalAmount(request.getTotalAmount());
        orderInfo.setThirdParty(ALIPAY_WEB);
        orderInfo.setPaymentStatus(WAIT_BUYER_PAY);
        orderInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
        //获取设备信息
        DeviceInfo deviceInfo = IpUtils.getDeviceInfo();
        BeanUtils.copyProperties(deviceInfo, orderInfo);
//        orderInfo.setIpAddr(deviceInfo.getIpaddr());
        Date nowDate = DateUtils.getNowDate();
        orderInfo.setCreateTime(nowDate);
        orderInfo.setOrderType(PoOrderTypeEnum.ORDER_TYPE_0.getValue());

        //充值记录信息
        PointsRechargeInfo rechargeInfo = new PointsRechargeInfo();
        rechargeInfo.setRechargeId(IdUtils.snowflakeId().toString());
        rechargeInfo.setPackageId(request.getPackageId());
        rechargeInfo.setUserId(request.getUserId());
        rechargeInfo.setOrderId(request.getOutTradeNo());
        //先写死
        rechargeInfo.setRechargeCount(1L);
        Long points = StringUtils.isNotNull(packageInfo.getPoints()) ? packageInfo.getPoints() : 0L;
        Long bonus = StringUtils.isNotNull(packageInfo.getPointsBonus()) ? packageInfo.getPointsBonus() : 0L;
        rechargeInfo.setTotalCount(points + bonus);
        rechargeInfo.setPointsCount(points);
        rechargeInfo.setBonusCount(bonus);
        rechargeInfo.setPaymentType(PoPaymentTypeEnum.PAYMENT_TYPE_0.getValue());
        rechargeInfo.setPriceCount(request.getTotalAmount());
        rechargeInfo.setBuyerPayAmount(new BigDecimal(BigInteger.ZERO));
        rechargeInfo.setThirdParty(ALIPAY_WEB);
        rechargeInfo.setRechargeStatus(PoRechargeStatusEnum.RECHARGE_STATUS_0.getValue());
        BeanUtils.copyProperties(deviceInfo, rechargeInfo);
//        rechargeInfo.setIpAddr(deviceInfo.getIpaddr());
        rechargeInfo.setCreateTime(nowDate);

        transactionTemplate.execute(result -> {
            paymentOrderInfoService.save(orderInfo);
            pointsRechargeInfoService.save(rechargeInfo);
            return true;
        });
    }

    @Override
    public String alipayCallback(HashMap<String, String> map) {
        boolean sign = false;
        try {
            sign = AlipaySignature.rsaCheckV1(map,
                    alipayPaymentConfig.getPublicKey(),
                    alipayPaymentConfig.getCharset(),
                    alipayPaymentConfig.getSignType());
        } catch (AlipayApiException e) {
            log.error("时间：{}获取支付宝签名失败：{}", DateUtils.getNowDate(), JSON.toJSONString(e));
//            throw new RuntimeException("获取支付宝签名失败！！！");
            //记录日志
            errorLogInfoService.saveErrorLogInfo(
                    null,
                    PoPaymentTypeEnum.PAYMENT_TYPE_0.getValue(),
                    ALIPAY_WEB,
                    PoOrderTypeEnum.ORDER_TYPE_0.getValue(),
                    null,
                    null,
                    PoErrorLogTypeEnum.ERROR_LOG_TYPE_2.getValue(),
                    null, null, e);
            return alipayPaymentConfig.getRedirectUrl();
        }

        if (!sign) {
            log.error("时间：{}签名验证失败：{}", DateUtils.getNowDate(), sign);
            //记录日志
            errorLogInfoService.saveErrorLogInfo(
                    null,
                    PoPaymentTypeEnum.PAYMENT_TYPE_0.getValue(),
                    ALIPAY_WEB,
                    PoOrderTypeEnum.ORDER_TYPE_0.getValue(),
                    null,
                    null,
                    PoErrorLogTypeEnum.ERROR_LOG_TYPE_2.getValue(),
                    null,
                    null,
                    sign);
            return alipayPaymentConfig.getRedirectUrl();
        }
        //转换map为json
        String json = JSON.toJSONString(map);
        //转换JSON为阿里支付回调请求参数实体
        AlipayCallbackRequest alipayCallbackRequest = JSON.parseObject(json, AlipayCallbackRequest.class); //转换map为json
        getAlipayOrder(alipayCallbackRequest.getOutTradeNo(), alipayCallbackRequest.getTradeNo());
        return alipayPaymentConfig.getRedirectUrl();
    }

    private PaymentOrderInfo getAlipayOrder(String outTradeNo, String tradeNo) {
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayManager.query(outTradeNo, tradeNo);
            if (StringUtils.isNotNull(response) && response.isSuccess()) {
                //执行积分充值
                PaymentOrderInfo paymentOrderInfo = executePointsRecharge(response);
                //删除缓存
                if (StringUtils.isNotNull(paymentOrderInfo)) {
                    redisCache.deleteObject(POINTS_ORDER_DETAIL + paymentOrderInfo.getOrderId() + COMMON_SEPARATOR_CACHE + paymentOrderInfo.getUserId());
                    return paymentOrderInfo;
                }
            } else if (StringUtils.isNotNull(response) && response.getCode().equals("40004")) {
                return null;
            } else {
                //失败
                log.error("时间：{}支付宝回调失败：{}", DateUtils.getNowDate(), response.getMsg());
                //记录日志
                errorLogInfoService.saveErrorLogInfo(
                        null,
                        PoPaymentTypeEnum.PAYMENT_TYPE_0.getValue(),
                        ALIPAY_WEB,
                        PoOrderTypeEnum.ORDER_TYPE_0.getValue(),
                        tradeNo,
                        outTradeNo,
                        PoErrorLogTypeEnum.ERROR_LOG_TYPE_1.getValue(),
                        StringUtils.isNotEmpty(response.getCode()) ? response.getCode() : "",
                        StringUtils.isNotEmpty(response.getMsg()) ? response.getMsg() + (StringUtils.isNotEmpty(response.getSubMsg()) ? response.getSubMsg() : "") : "",
                        response);
            }
        } catch (Exception e) {
            log.error("时间：{}支付宝回调失败：{}", DateUtils.getNowDate(), JSON.toJSONString(e));
            //记录日志
            boolean notNull = StringUtils.isNotNull(response);
            errorLogInfoService.saveErrorLogInfo(null,
                    PoPaymentTypeEnum.PAYMENT_TYPE_0.getValue(),
                    ALIPAY_WEB,
                    PoOrderTypeEnum.ORDER_TYPE_0.getValue(),
                    tradeNo,
                    outTradeNo,
                    PoErrorLogTypeEnum.ERROR_LOG_TYPE_1.getValue(),
                    notNull && StringUtils.isNotEmpty(response.getCode()) ? response.getCode() : "",
                    notNull && StringUtils.isNotEmpty(response.getMsg()) ? response.getMsg() + (StringUtils.isNotEmpty(response.getSubMsg()) ? response.getSubMsg() : "") : "",
                    notNull ? response : e.getMessage());
        }
        return null;
    }

    private PaymentOrderInfo executePointsRecharge(AlipayTradeQueryResponse response) {
        //查询到对应支付订单
        PaymentOrderInfo paymentOrderInfo = paymentOrderInfoService.selectPaymentOrderInfoByOrderId(response.getOutTradeNo());
        Date nowDate = DateUtils.getNowDate();
        if (StringUtils.isNull(paymentOrderInfo)) {
            log.error("时间:{}未找到对应支付订单:{}", nowDate, response.getOutTradeNo());
            return null;
        }
        //如果是已经支付成功直接返回
        if (PoOrderStatusEnum.ORDER_STATUS_1.getValue().equals(paymentOrderInfo.getOrderStatus())) {
            log.error("时间:{}该订单已经支付成功:{}", nowDate, response.getOutTradeNo());
            return paymentOrderInfo;
        }
        RLock rLock = redissonClient.getLock(POINTS_ORDER_LOCK + response.getOutTradeNo());
        boolean locked = false;
        try {
            locked = rLock.tryLock(5, 30, TimeUnit.SECONDS);
            if (locked) {
                return updatePaymentOrderInfo(response, paymentOrderInfo, nowDate);
            }
            // 没有获取锁，返回信息
            return paymentOrderInfo;

        } catch (InterruptedException e) {
            log.error("获取锁被中断，订单: {}", response.getOutTradeNo(), e);
            Thread.currentThread().interrupt();
            return paymentOrderInfo;
        } finally {
            if (locked) {
                rLock.unlock();
            }
        }
    }

    private PaymentOrderInfo updatePaymentOrderInfo(AlipayTradeQueryResponse response, PaymentOrderInfo paymentOrderInfo, Date nowDate) {
        //查询到对应的积分充值记录
        String userId = paymentOrderInfo.getUserId();
        PointsRechargeInfo rechargeInfo = pointsRechargeInfoService.getOne(new LambdaQueryWrapper<PointsRechargeInfo>()
                .eq(PointsRechargeInfo::getOrderId, response.getOutTradeNo())
                .eq(PointsRechargeInfo::getUserId, userId));

        if (StringUtils.isNull(rechargeInfo)) {
            log.error("时间:{}未找到对应的积分充值记录：{}", nowDate, response.getOutTradeNo());
            return null;
        }
        //设置订单信息
        String totalAmount = response.getTotalAmount();
        if (StringUtils.isEmpty(totalAmount)) {
            totalAmount = "0";
        }
        paymentOrderInfo.setTotalAmount(new BigDecimal(totalAmount));
        String buyerPayAmount = response.getBuyerPayAmount();
        if (StringUtils.isEmpty(buyerPayAmount)) {
            buyerPayAmount = "0";
        }
        BigDecimal rechargeAmount = new BigDecimal(buyerPayAmount);
        paymentOrderInfo.setBuyerPayAmount(rechargeAmount);
        String receiptAmount = response.getReceiptAmount();
        if (StringUtils.isEmpty(receiptAmount)) {
            receiptAmount = "0";
        }
        paymentOrderInfo.setReceiptAmount(new BigDecimal(receiptAmount));
        String discountAmount = response.getDiscountAmount();
        if (StringUtils.isEmpty(discountAmount)) {
            discountAmount = "0";
        }
        paymentOrderInfo.setDiscountAmount(new BigDecimal(discountAmount));
        paymentOrderInfo.setThirdUserId(response.getBuyerUserId());
        paymentOrderInfo.setThirdPartyOrder(response.getTradeNo());
        paymentOrderInfo.setPaymentTime(response.getSendPayDate());
        paymentOrderInfo.setPaymentStatus(response.getTradeStatus());
        paymentOrderInfo.setPaymentCode(response.getCode());
        paymentOrderInfo.setPaymentMsg(response.getMsg());
        paymentOrderInfo.setPaymentExtend(JSON.toJSONString(response));
        paymentOrderInfo.setUpdateTime(nowDate);

        //设置积分充值信息
        rechargeInfo.setBuyerPayAmount(rechargeAmount);
        rechargeInfo.setThirdPartyOrder(response.getTradeNo());
        rechargeInfo.setArrivalTime(nowDate);
        rechargeInfo.setUpdateTime(nowDate);

        if (PayConstants.TRADE_SUCCESS.equals(response.getTradeStatus())) {
            paymentOrderInfo.setOrderStatus(PoOrderStatusEnum.ORDER_STATUS_1.getValue());
            rechargeInfo.setRechargeStatus(PoRechargeStatusEnum.RECHARGE_STATUS_1.getValue());
//            System.out.println("支付成功");
        }
        if (PayConstants.TRADE_FINISHED.equals(response.getTradeStatus())) {
            paymentOrderInfo.setOrderStatus(PoOrderStatusEnum.ORDER_STATUS_1.getValue());
            rechargeInfo.setRechargeStatus(PoRechargeStatusEnum.RECHARGE_STATUS_1.getValue());
//            System.out.println("支付成功");
        }
        if (PayConstants.TRADE_CLOSED.equals(response.getTradeStatus())) {
            paymentOrderInfo.setOrderStatus(PoOrderStatusEnum.ORDER_STATUS_3.getValue());
            rechargeInfo.setRechargeStatus(PoRechargeStatusEnum.RECHARGE_STATUS_3.getValue());
//            System.out.println("支付关闭");
        }
        if (PayConstants.WAIT_BUYER_PAY.equals(response.getTradeStatus())) {
            rechargeInfo.setRechargeStatus(PoRechargeStatusEnum.RECHARGE_STATUS_0.getValue());
            paymentOrderInfo.setOrderStatus(PoOrderStatusEnum.ORDER_STATUS_0.getValue());
//            System.out.println("尚未支付");
        }
//        System.out.println("paymentResponse = " + JSON.toJSONString(response));
        //查询到用户账户
        AccountInfo accountInfo = accountInfoService.selectAccountInfoByUserId(userId);
        //判断用户账户是否存在
        if (StringUtils.isNull(accountInfo)) {
            //创建账户
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
        }

        //积分使用记录
        PointsUsageLogInfo pointsUsageLogInfo = new PointsUsageLogInfo();
        //用户使用前积分 就是用户原本余额
        pointsUsageLogInfo.setPointsBefore(accountInfo.getPointsBalance());
        pointsUsageLogInfo.setLogType(PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_0.getValue());
        pointsUsageLogInfo.setUserId(rechargeInfo.getUserId());
        pointsUsageLogInfo.setTargetId(rechargeInfo.getRechargeId());
        //消费积分 这里就是充值的积分
        pointsUsageLogInfo.setPointsUsed(rechargeInfo.getTotalCount());
        pointsUsageLogInfo.setDeviceId(rechargeInfo.getDeviceId());
        pointsUsageLogInfo.setBrowser(rechargeInfo.getBrowser());
        pointsUsageLogInfo.setOs(rechargeInfo.getOs());
        pointsUsageLogInfo.setPlatform(rechargeInfo.getPlatform());
        pointsUsageLogInfo.setIpAddr(rechargeInfo.getIpAddr());
        pointsUsageLogInfo.setIpAddress(rechargeInfo.getIpAddress());
        pointsUsageLogInfo.setCreateTime(rechargeInfo.getArrivalTime());
        pointsUsageLogInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());

        //判断是成功还是失败
        if (PayConstants.TRADE_SUCCESS.equals(response.getTradeStatus()) || PayConstants.TRADE_FINISHED.equals(response.getTradeStatus())) {
            //成功
            accountInfo.setPointsBalance(accountInfo.getPointsBalance() + rechargeInfo.getTotalCount());
            accountInfo.setRechargeAmount(accountInfo.getRechargeAmount().add(rechargeInfo.getPriceCount()));
        }
        //用户使用后积分 就是添加后的用户积分，如果支付未成功也是原积分
        pointsUsageLogInfo.setPointsAfter(accountInfo.getPointsBalance());

        final AccountInfo accountInfoFinal = accountInfo;
        transactionTemplate.execute(task -> {
            pointsRechargeInfoService.updateById(rechargeInfo);
            accountInfoService.saveOrUpdate(accountInfoFinal);
            paymentOrderInfoService.updateById(paymentOrderInfo);
            pointsUsageLogInfoService.save(pointsUsageLogInfo);
            return null;
        });
        return paymentOrderInfo;
    }

    @Override
    public UserPaymentOrderInfoVo getAlipayWebOrder(String outTradeNo, String userId) {
        String key = POINTS_ORDER_DETAIL + outTradeNo + COMMON_SEPARATOR_CACHE + userId;
        UserPaymentOrderInfoVo cache = redisCache.getCacheObject(key);
        if (StringUtils.isNotNull(cache)) {
            return cache;
        }
        //根据用户和订单编号查询订单信息
        PaymentOrderInfo orderInfo = paymentOrderInfoService.getOne(new LambdaQueryWrapper<PaymentOrderInfo>()
                .eq(PaymentOrderInfo::getUserId, userId)
                .eq(PaymentOrderInfo::getOrderId, outTradeNo));
        if (StringUtils.isNotNull(orderInfo)) {
            //存入缓存
            UserPaymentOrderInfoVo userPaymentOrderInfoVo = UserPaymentOrderInfoVo.objToVo(orderInfo);
            redisCache.setCacheObject(key, userPaymentOrderInfoVo);
            return userPaymentOrderInfoVo;
        }
        //如果还是没有订单信息
        return null;
    }

    @Override
    public UserPaymentOrderInfoVo getAlipayWeb(String outTradeNo, String userId) {
        PaymentOrderInfo alipayOrder = getAlipayOrder(outTradeNo, null);
        return UserPaymentOrderInfoVo.objToVo(alipayOrder);
    }
}
