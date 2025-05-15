package com.lz.points.controller.user;

import com.lz.common.core.domain.AjaxResult;
import com.lz.points.model.dto.pay.PayRequest;
import com.lz.points.service.IPayService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 支付
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-15  09:38
 * @Version: 1.0
 */
@RestController
@RequestMapping("/user/points/pay")
public class UserPayController extends BaseUserInfoController {
    @Resource
    private IPayService payService;

    /**
     * 支付宝支付
     */
     @PreAuthorize("@uss.hasPermi('points:payment')")
    @PostMapping("/alipay/web")
    public AjaxResult alipayWeb(@RequestBody PayRequest payRequest) {
         payRequest.setUserId(getUserId());
        return success(payService.alipayWeb(payRequest));
    }
}
