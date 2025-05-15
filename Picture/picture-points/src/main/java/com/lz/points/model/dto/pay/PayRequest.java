package com.lz.points.model.dto.pay;

import lombok.Data;

/**
 * 支付请求
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-15  09:43
 * @Version: 1.0
 */
@Data
public class PayRequest {
    private String packageId;
    private String userId;
}
