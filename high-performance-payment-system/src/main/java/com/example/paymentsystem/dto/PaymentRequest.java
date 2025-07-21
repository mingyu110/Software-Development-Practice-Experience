package com.example.paymentsystem.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 代表支付请求的数据传输对象 (DTO)。
 */
@Data
public class PaymentRequest {

    /**
     * 订单的唯一标识符。
     */
    private String orderId;

    /**
     * 支付金额。
     */
    private BigDecimal amount;

    /**
     * 支付货币单位。
     */
    private String currency;

    /**
     * 发起支付的用户的ID。
     */
    private String userId;
}