package com.example.paymentsystem.controller;

import com.example.paymentsystem.dto.PaymentRequest;
import com.example.paymentsystem.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于处理支付请求的 REST 控制器。
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * 处理一个支付请求。
     *
     * @param paymentRequest 支付请求的详细信息。
     * @return 一个响应实体，表明支付处理的结果。
     */
    @PostMapping
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequest paymentRequest) {
        paymentService.processPayment(paymentRequest);
        return ResponseEntity.ok("支付请求已收到，正在处理中。");
    }
}