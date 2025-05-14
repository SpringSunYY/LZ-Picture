package com.lz.points.manager.model;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

/**
 * TODO
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-14  22:04
 * @Version: 1.0
 */
@Data
public class AlipayPcPaymentResponse {
    private String pageRedirectionData;
    private String msg;
    private String body;
}
